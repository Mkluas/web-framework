package cn.mklaus.framework.config.exception;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * @author Mklaus
 * @date 2018-07-24 上午11:40
 */
public class ErrorControllerRegister implements ApplicationContextInitializer {
    /**
     * 提前注册DefaultErrorController
     *
     * BasicErrorController会在没有ErrorController类型的Bean的条件下注册生效，
     * 若不提前注册DefaultErrorController，则会出现冲突，存在两个ErrorController. 【/error】重复。
     *
     * @param context ConfigurableApplicationContext
     */
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        GenericWebApplicationContext c = (GenericWebApplicationContext) context;
        c.registerBeanDefinition(DefaultErrorController.class.getName(), new RootBeanDefinition(DefaultErrorController.class));
    }
}
