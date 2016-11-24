package me.ridog.weixin.service;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.ridog.weixin.config.WxAuthConfig;
import me.ridog.weixin.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Tate on 2016/10/25.
 */

@Service
public class WxService extends WxMpServiceImpl {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    LogHandler logHandler;
    @Autowired
    SubscribeHandler subscribeHandler;
    @Autowired
    UnSubscribeHandler unSubscribeHandler;
    @Autowired
    MenuClickHandler menuClickHandler;
    @Autowired
    MessageHandler messageHandler;
    @Autowired
    WxAuthConfig wxAuthConfig;

    private WxMpMessageRouter router;

    @PostConstruct
    public void init() {
        this.setWxMpConfigStorage(wxAuthConfig.wxMpConfigStorage());
        this.refreshRouter();
    }

    private void refreshRouter(){
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(this);

        // 记录所有事件的日志
        newRouter.rule().handler(this.logHandler).next();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.BUTTON_CLICK).handler(menuClickHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SUBSCRIBE).handler(subscribeHandler)
                .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_UNSUBSCRIBE).handler(unSubscribeHandler)
                .end();

        // 默认
        newRouter.rule().async(false).handler(messageHandler).end();

        this.router = newRouter;
    }

    public WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return router.route(message);
        } catch (Exception e) {
           logger.error(e.getMessage(), e);
        }

        return null;
    }

}
