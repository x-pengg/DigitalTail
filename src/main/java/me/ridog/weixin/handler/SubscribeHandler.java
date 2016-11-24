package me.ridog.weixin.handler;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.ridog.weixin.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * 关注事件
 *
 * Created by Tate on 2016/10/25.
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Autowired
    WxService wxService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) throws WxErrorException {

        logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        WxMpUser userWxInfo = wxService.getUserService().userInfo(wxMessage.getFromUser(), null);
        if (userWxInfo != null) {
            logger.info(userWxInfo.getNickname()+" 老哥，欢迎你！");
        }

        return  WxMpXmlOutMessage.TEXT()
                .content("骚东西，你好！")
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
    }

}
