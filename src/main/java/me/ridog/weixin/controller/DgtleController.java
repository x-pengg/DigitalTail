package me.ridog.weixin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Tate on 2016/11/7.
 */
@Controller
@RequestMapping("dgtle")
public class DgtleController {

    @Autowired
    private CommController commController;

    @RequestMapping(value = "articles", method = RequestMethod.GET)
    public String index(){
        return "dgtle/index";
    }

    @RequestMapping(value = "homeTpl", method = RequestMethod.GET)
    public String homeTpl(Model model){
        model.addAttribute("data", commController.queryPage(10, 0));
        model.addAttribute("hot", commController.getHotArticle());
        return "dgtle/home.tpl";
    }

}
