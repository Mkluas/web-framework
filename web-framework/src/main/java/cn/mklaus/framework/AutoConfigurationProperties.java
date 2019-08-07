package cn.mklaus.framework;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Mklaus
 * Created on 2018-03-29 上午10:05
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.config")
public class AutoConfigurationProperties implements ApplicationContextAware {

    private String basepackage;

    private boolean performance;

    private boolean logRequest = true;

    private boolean useDefaultResourceHandler = true;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if ((basepackage != null && basepackage.length() > 0)) {
            return;
        }

        String[] springBootApplicationAnnotations = applicationContext.getBeanNamesForAnnotation(SpringBootApplication.class);
        if (springBootApplicationAnnotations.length > 0) {
            Object bean = applicationContext.getBean(springBootApplicationAnnotations[0]);
            basepackage = bean.getClass().getPackage().getName();
        }
    }

}
