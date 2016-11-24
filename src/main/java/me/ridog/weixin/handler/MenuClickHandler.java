package me.ridog.weixin.handler;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.ridog.weixin.dto.WxMenu;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Created by Tate on 2016/10/25.
 */
@Component
public class MenuClickHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) throws WxErrorException {

        String key = wxMessage.getEventKey();
        WxMenu menu = null;
        try {
            menu = gson.fromJson(key, WxMenu.class);
            return WxMpXmlOutMessage.TEXT()
                    .content(menu.getContent())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();

        } catch (Exception e) {
            return WxMpXmlOutMessage.TEXT().content(key)
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
        }

    }
}
