package cn.mklaus.framework.config.monitor;

import cn.mklaus.framework.config.AutoConfigurationProperties;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;


/**
 * @author Mklaus
 * Created on 2018-01-13 下午4:21
 */
@Configuration
@EnableAspectJAutoProxy
@Aspect
@ConditionalOnProperty(prefix = "cn.mklaus.config", value = "performance", havingValue = "true", matchIfMissing = true)
public class PerformanceLogConfiguration {

    @Resource
    private AutoConfigurationProperties properties;

    @Bean
    public AbstractMonitoringInterceptor performanceMonitorInterceptor() {
        return new AbstractMonitoringInterceptor() {
            @Override
            protected Object invokeUnderTrace(MethodInvocation invocation, Log log) throws Throwable {
                String name = this.createInvocationTraceName(invocation);
                StopWatch stopWatch = new StopWatch(name);
                stopWatch.start(name);
                Object var5;
                try {
                    var5 = invocation.proceed();
                } finally {
                    stopWatch.stop();
                    log.info(stopWatch.shortSummary());
                }
                return var5;
            }

            @Override
            protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
                return true;
            }
        };
    }

    @Bean
    public Advisor performanceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(String.format("execution(public * %s..*Service.*(..))", properties.getBasepackage()));
        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
    }

}
