package me.ridog.weixin.handler;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * 对所有接收到的消息输出日志，也可进行持久化处理
 *
 * Created by Tate on 2016/10/25.
 */
@Component
public class LogHandler extends AbstractHandler{

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) throws WxErrorException {
        logger.info("\n接收到请求消息，内容：【{}】", wxMpXmlMessage.toString());
        return null;
    }

}
