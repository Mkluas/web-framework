package cn.mklaus.framework.monitor;

import cn.mklaus.framework.web.Response;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mklaus
 * Created on 2018-01-29 下午12:46
 */
@Slf4j
@Aspect
@Order(1)
@Component
@ConditionalOnProperty(prefix = "cn.mklaus.config", value = "logRequest", matchIfMissing = true, havingValue = "true")
public class WebLogAspect {

    public static final String HIDE_RESPONSE_LOG = "HIDE_RESPONSE_LOG";

    @Pointcut("execution(public * *..web..*Controller.*(..))")
    public void controllerPointcut(){}

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("--------");
        log.info("URL    : {} {}", request.getRequestURI(), request.getMethod());
        log.info("METHOD : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
        log.info("ARGS   : {}", JSON.toJSONString(request.getParameterMap()));
        log.info("--------");
    }

    @AfterReturning(returning = "ret", pointcut = "controllerPointcut()")
    public void doAfterReturning(Object ret) {
        if (ret instanceof Response) {
            Response resp = (Response)ret;
            if (resp.getErrCode() == 0 && resp.getData().getBooleanValue(HIDE_RESPONSE_LOG)) {
                log.info("RESPONSE : { errCode: 0, errMsg: ok, hide response log }");
                return;
            }
        }
        log.info("RESPONSE : " + ret);
    }

}
