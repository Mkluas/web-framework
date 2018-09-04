package cn.mklaus.framework.config;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author Mklaus
 * @date 2018-01-06 下午12:08
 */
@ConditionalOnProperty(prefix = "cn.mklaus.config", value = "eventPublisher", havingValue = "true", matchIfMissing = true)
@Component
public class EventPublisher implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void publishEvent(ApplicationEvent event) {
        this.applicationContext.publishEvent(event);
    }

}
