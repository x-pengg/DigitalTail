package me.ridog.weixin.exception;

import me.ridog.weixin.config.ErrorCode;
import me.ridog.weixin.dto.APIResult;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by chan on 2016/11/4.
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String rs = errMsg(ex);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        try {
            writer = response.getWriter();
            writer.write(rs);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
        }

        return null;
    }

    public String errMsg(Exception ex){
        if (ex instanceof APIException) {
            return APIResult.newRs().fail().errCode(((APIException) ex).getCode()).errMsg(((APIException) ex).getMsg()).build();
        } else if (ex instanceof IllegalArgumentException || ex instanceof MissingServletRequestParameterException) {
            return APIResult.newRs().fail().errCode(ErrorCode.PARAM_ERROR).errMsg("参数错误").build();
        }else{
            return APIResult.newRs().fail().errCode(ErrorCode.SYSTEM_BUSY).errMsg("系统繁忙，请稍后再试!\r\n"+ex.toString()).build();
        }

    }

}
