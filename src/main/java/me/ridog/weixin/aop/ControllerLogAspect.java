package me.ridog.weixin.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tate on 2016/10/25.
 */
@Aspect
@Component
public class ControllerLogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("within(me.ridog.weixin.controller..*)")
    public void inController() {
    }

    @Pointcut("execution(public * me.ridog.weixin.controller..*.*(..))")
    public void controller(){
    }

    @Before("inController()")
    public void writeBeforeLog(JoinPoint jp) {
        this.debugInController(jp, "Start");
    }

    @After("inController()")
    public void writeAfterLog(JoinPoint jp) {
        this.debugInController(jp, "End");
    }

    @Before("controller()")
    public void writeParams(JoinPoint jp) {
        String[] names = ((CodeSignature) jp.getSignature())
                .getParameterNames();
        Object[] args = jp.getArgs();

        if (ArrayUtils.isEmpty(names)) {
            return;
        }

        StringBuilder sb = new StringBuilder("Arguments: ");
        for (int i = 0; i < names.length; i++) {
            sb.append(names[i] + " = " + args[i] + ",");
        }

        debugInController(jp, sb.toString());
    }
    private void debugInController(JoinPoint jp, String msg) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        this.logger.debug("\n【{}】{}.{}() {} ", request.getRemoteAddr(),
                jp.getTarget()
                        .getClass().getSimpleName(), jp.getSignature().getName(), msg);
    }

}
