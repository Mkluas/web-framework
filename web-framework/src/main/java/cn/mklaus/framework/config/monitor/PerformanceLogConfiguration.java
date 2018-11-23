package cn.mklaus.framework.config.monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

/**
 * @author Mklaus
 * Created on 2018-01-13 下午4:21
 */
@Aspect
@Configuration
@ConditionalOnProperty(prefix = "cn.mklaus.config", value = "performance", havingValue = "true", matchIfMissing = true)
public class PerformanceLogConfiguration {

    private static Logger log = LoggerFactory.getLogger(PerformanceLogConfiguration.class);

    @Pointcut("execution(public * *..service..*ServiceImpl.*(..))")
    public void servicePointcut() {}

    @Around("servicePointcut()")
    public Object logServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable{
        String name = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        StopWatch stopWatch = new StopWatch(name);
        stopWatch.start(name);
        Object ret;
        try {
            ret = joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info(stopWatch.shortSummary());
        }
        return ret;
    }

}
