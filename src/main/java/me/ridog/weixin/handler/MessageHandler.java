package me.ridog.weixin.handler;

import com.google.gson.Gson;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.ridog.weixin.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

/**
 * Created by Tate on 2016/10/25.
 */
@Component
public class MessageHandler extends AbstractHandler {

    @Autowired
    WxService wxService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) throws WxErrorException {

        WxMpUser userWxInfo = wxService.getUserService().userInfo(wxMessage.getFromUser(), null);
        if (userWxInfo != null) {
            logger.info(userWxInfo.getNickname()+" 老哥，欢迎你！");
        }
        if (!wxMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT)) {

            String content = wxMessage.getContent();

            return  WxMpXmlOutMessage.TEXT()
                    .content(content)
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        }
        return null;
    }

}
