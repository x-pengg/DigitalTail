package me.ridog.weixin.controller.api;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import me.ridog.weixin.controller.CommController;
import me.ridog.weixin.dto.Article;
import me.ridog.weixin.repository.mongodb.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created by chan on 2016/10/28.
 */
@RestController
@RequestMapping("dgtle")
public class DgtleApiController {

    @Autowired ArticleRepository articleRepository;

    @Autowired
    private CommController commController;

    @Autowired
    private Gson gson;


    @RequestMapping(value = "getArticles/{pageSize:[0-9]+}/{pageNumber:[0-9]+}", method = RequestMethod.GET)
    public HashMap<String, Object> getArticles(@PathVariable("pageSize") int pageSize, @PathVariable("pageNumber") int pageNumber){
        return commController.queryPage(pageSize, pageNumber);
    }

    @RequestMapping(value = "getArticle/{id:[0-9]+}", method = RequestMethod.GET)
    public Article getArticles(@PathVariable("id") String id){
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return articleRepository.findById(id);
    }

    @RequestMapping(value = "getHot", method = RequestMethod.GET)
    public String getHot(){
        return gson.toJson(commController.getHotArticle());
    }

}
