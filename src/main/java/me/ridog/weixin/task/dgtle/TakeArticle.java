package me.ridog.weixin.task.dgtle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.ridog.weixin.dto.Article;
import me.ridog.weixin.repository.mongodb.ArticleRepository;
import me.ridog.weixin.util.JsonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Tate on 2016/10/27.
 */
@Component
public class TakeArticle {

    @Autowired
    private ArticleRepository articleRepository;

    Logger logger = LoggerFactory.getLogger(getClass());

    static Gson gson = new Gson();

    final int PAGES = 10; // 总页数
    final int PAGE_SIZE = 50; // 页大小
    HashSet<String> dataSet = new HashSet<>();
    ArrayBlockingQueue<Article> queue = new ArrayBlockingQueue<Article>(50);


   @PostConstruct
    public void working(){
        this.addToQueue();
        this.saveToMongoDB();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(5*60*1000);
                        final String API_URL = "http://www.dgtle.com/api/dgtle_api/v1/api.php?actions=article&apikeys=" +
                                "DGTLECOM_APITEST1&charset=UTF8&dataform=json&inapi=yes&limit="+
                                "0_3&modules=portal&order=" +
                                "dateline_desc&platform=ios&swh=480x800&version=3.1.5";
                        String json = JsonUtil.getJSON(API_URL);
                        HashMap<String, Article> map = getArticle(json);
                        map.forEach((k, v)->{
                            if (!dataSet.contains(k)){
                                logger.info("发现新数据："+ v.getTitle());
                                dataSet.add(k);
                                try {
                                    queue.put(v);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "monitor").start();
    }

    public void saveToMongoDB(){
        for (int i = 0; i < 8; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true){
                            Article article = queue.take();
                            try {
                                Document document = Jsoup.connect("http://www.dgtle.com/forum.php?mod=viewthread&tid="+article.getId()).get();
                                Elements pcb = document.getElementsByClass("group_viewbox_body");
                                article.setHtml(pcb.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            articleRepository.save(article);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"db-"+i).start();
        }
    }

  public void addToQueue(){

      for (int i = 0; i < PAGES; i++) {
          new Thread(new Runnable() {
              @Override
              public void run() {
                  final String API_URL = "http://www.dgtle.com/api/dgtle_api/v1/api.php?actions=article&apikeys=" +
                          "DGTLECOM_APITEST1&charset=UTF8&dataform=json&inapi=yes&limit="+
                          Thread.currentThread().getName()+"_"+PAGE_SIZE+"&modules=portal&order=" +
                          "dateline_desc&platform=ios&swh=480x800&version=3.1.5";
                  String json = JsonUtil.getJSON(API_URL);
                  HashMap<String, Article> map = getArticle(json);
                  map.forEach((k, v)->{
                      dataSet.add(k);
                      try {
                          queue.put(v);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  });
              }
          }, i*PAGE_SIZE+"").start();
      }
  }

    private static HashMap<String, Article> getArticle(String json){
        JsonObject o1 = gson.fromJson(json, JsonObject.class);
        JsonObject returnData = o1.getAsJsonObject("returnData");
        JsonObject articleList = returnData.getAsJsonObject("articlelist");
        return gson.fromJson(articleList, new TypeToken<HashMap<String, Article>>() {
        }.getType());
    }


}
