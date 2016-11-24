package me.ridog.weixin.controller;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Tate on 2016/11/8.
 */
@Controller
@RequestMapping("msg")
public class TestController {

    @Autowired
    private WxMpService wxMpService;

    @RequestMapping("")
    @ResponseBody
    public String send(){

        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setToUser("okyYYwrqmIpne5CtBGhvIN-uSnY4");
        //wxMpTemplateMessage.setToUser("okyYYwiefJwC7DyRoBcg_2EiSYQg");
        wxMpTemplateMessage.setUrl("https://ridog.me");
        wxMpTemplateMessage.setTemplateId("9bklcSzppwS8ZfJZLj9ExKeRbBqj3paK1C6LfsyGJlI");
        wxMpTemplateMessage.getData().add(new WxMpTemplateData("msg","宋佳欣？喵喵喵？"));
        try {
           while (true){
               Thread.sleep(2000);
               wxMpService.templateSend(wxMpTemplateMessage);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ok";
    }
}
