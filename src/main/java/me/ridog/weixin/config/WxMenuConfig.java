package me.ridog.weixin.config;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.ridog.weixin.service.WxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tate on 2016/10/25.
 */
@Component
public class WxMenuConfig {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    WxService wxService;

    @PostConstruct
    private void init() {
        logger.info("初始化菜单..");
        try {
            if (!System.getProperty("os.name").startsWith("Windows")) {
                menuDelete();
                wxService.getMenuService().menuCreate(this.getMenu());
            }
        } catch (Exception e) {
            logger.info("菜单创建出现异常", e);
        }
    }

    private void menuDelete() throws WxErrorException {
        wxService.getMenuService().menuDelete();
        logger.info("删除菜单成功");
    }

    protected WxMenu getMenu() {

        WxMenu menu = new WxMenu();
        WxMenuButton button1 = new WxMenuButton();
        button1.setKey(MenuKey.DGTLE.toString());
        button1.setType(WxConsts.BUTTON_VIEW);
        button1.setUrl("http://t66y.tunnel.qydev.com/dgtle/articles");
        button1.setName("数字尾巴");


        WxMenuButton button2 = new WxMenuButton();
        button2.setKey(MenuKey.BOON.toString());
        button2.setName("福利");
        button2.setType(WxConsts.BUTTON_CLICK);

        WxMenuButton button3 = new WxMenuButton();
        button3.setKey(MenuKey.OFFER.toString());
        button3.setName("贡献");
        button3.setType(WxConsts.BUTTON_CLICK);


        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);

        return menu;
    }

}
