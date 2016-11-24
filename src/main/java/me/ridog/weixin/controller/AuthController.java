package me.ridog.weixin.controller;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.ridog.weixin.service.WxService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tate on 2016/10/25.
 */
@RestController
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    WxMpConfigStorage configStorage;
    @Autowired
    WxMpService wxMpService;
    @Resource
    WxService wxService;


    @RequestMapping(value = "auth")
    public void wechatCore(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echoStr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            String echoStrOut = String.copyValueOf(echoStr.toCharArray());
            response.getWriter().println(echoStrOut);
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type"))
                ? "raw"
                : request.getParameter("encrypt_type");

            if ("raw".equals(encryptType)) {
                // 明文传输的消息
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
                WxMpXmlOutMessage outMessage = wxService.route(inMessage);
                if (outMessage!=null) {
                    response.getWriter().write(outMessage.toXml());
                }
                return;
            }

            if ("aes".equals(encryptType)) {
                // 是aes加密的消息
                String msgSignature = request.getParameter("msg_signature");
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
                        request.getInputStream(), this.configStorage, timestamp, nonce,
                        msgSignature);
                this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
                WxMpXmlOutMessage outMessage = wxService.route(inMessage);
                this.logger.info(response.toString());
                if (outMessage!=null) {
                    response.getWriter()
                            .write(outMessage.toEncryptedXml(this.configStorage));
                }
                return;
            }

        response.getWriter().println("不可识别的加密类型");
        return;
    }
}
