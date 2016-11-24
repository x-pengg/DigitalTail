package me.ridog.weixin.controller;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.ridog.weixin.config.ErrorCode;
import me.ridog.weixin.dto.Article;
import me.ridog.weixin.exception.APIException;
import me.ridog.weixin.repository.mongodb.ArticleRepository;
import me.ridog.weixin.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tate on 2016/11/7.
 */
@Component
public class CommController {

    @Autowired
    ArticleRepository articleRepository;

    public HashMap<String, Object> queryPage(int pageSize, int pageNumber) {
        if (pageSize<=0)
            throw new APIException(ErrorCode.PARAM_PAGEZISE_ERROR, "pageSize 必须大于0");
        Page<Article> pages = articleRepository.findAll(new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "_id")));
        HashMap<String, Object> map = new HashMap<>();
        map.put("articles", pages.getContent());
        map.put("number", pages.getNumber());
        map.put("size", pages.getSize());
        map.put("totalPages", pages.getTotalPages());
        map.put("totalElements", pages.getTotalElements());
        map.put("isFirst", pages.isFirst());
        map.put("isLast", pages.isLast());
        return map;
    }

    public List<Article> getHotArticle(){
        final String url = "http://www.dgtle.com/api/dgtle_api/v1/api.php?" +
                "actions=diydata&apikeys=DGTLECOM_APITEST1&bid=274&charset=UTF8&dataform=json" +
                "&inapi=yes&modules=portal&platform=ios&swh=480x800&version=3.1.5";

        String json = JsonUtil.getJSON(url);
        Gson gson = new Gson();
        JsonObject _json = gson.fromJson(json, JsonObject.class);
        JsonObject returnData = _json.getAsJsonObject("returnData");
        JsonObject blocklist = returnData.getAsJsonObject("blocklist");
        JsonObject obj_274 = blocklist.getAsJsonObject("274");
        HashMap<String, Article> hotArticleMap = gson.fromJson(obj_274, new TypeToken<HashMap<String, Article>>() {
        }.getType());
        ArrayList<Article> hotArticles = Lists.newArrayList();
        hotArticleMap.forEach((k,v)->{
            hotArticles.add(v);
        });
        return hotArticles;
    }
}
