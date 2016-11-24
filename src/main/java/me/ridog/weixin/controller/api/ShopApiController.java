package me.ridog.weixin.controller.api;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import me.ridog.weixin.config.ErrorCode;
import me.ridog.weixin.dto.APIResult;
import me.ridog.weixin.exception.APIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tate on 2016/11/4.
 */
@RestController
@RequestMapping("shop")
public class ShopApiController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getPrice(@RequestParam(value = "url", required = false) String url){
        if (Strings.isNullOrEmpty(url))
            throw new APIException(ErrorCode.PARAM_ERROR, "请输入网站,支持京东、天猫、淘宝、亚马逊、苏宁、当当、1号店、易迅、国美、新蛋等商品网址");
        HashMap price = null;
        try {
            price = takePrice(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return APIResult.newRs().success().data(price).build();
    }


   private HashMap takePrice(String url) throws IOException {
       URLConnection urlConnection = new URL("http://tool.manmanbuy.com/history.aspx?w=950&h=580&h2=420&m=1&e=1&tofanli=0&url="+url).openConnection();
       urlConnection.setDoOutput(false);
       urlConnection.setDoInput(true);
       // urlConnection.setRequestMethod("GET");
       urlConnection.setRequestProperty("DNT", "1");
       urlConnection.setRequestProperty("Proxy-Connection", "keep-alive");
       urlConnection.setRequestProperty("Cache-Control", "max-age=0");
       urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
       urlConnection.setRequestProperty("Accept-Encoding", "deflate");
       urlConnection.setRequestProperty("Host", "tool.manmanbuy.com");
       urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
       urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
       urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
       urlConnection.connect();
       logger.info(url);
       logger.info("历史价格爬取中...");
       BufferedReader gbk = new BufferedReader(new InputStreamReader((urlConnection).getInputStream(), "GBK"));

       StringBuffer sb = new StringBuffer();
       while (true) {
           String str = (gbk).readLine();
           if (str == null)
               break;
           sb.append(str);
       }

       Pattern p = Pattern.compile("\\d{4},\\d,\\d\\d\\),\\d{1,10}");
       Matcher m = p.matcher(sb.toString());
       HashMap<Object, Object> map = Maps.newHashMap();
       while(m.find()){
           String replaceStr = m.group().replace(",", "-");
           String[] split = replaceStr.split("\\)-");
           map.put(split[0],split[1]);
       }

       return map;
   }

}
