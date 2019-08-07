package cn.mklaus.framework.web;

import cn.org.rapid_framework.freemarker.directive.BlockDirective;
import cn.org.rapid_framework.freemarker.directive.ExtendsDirective;
import cn.org.rapid_framework.freemarker.directive.OverrideDirective;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Mklaus
 * Created on 2018-03-30 上午11:00
 */
@ConditionalOnClass({
        freemarker.template.Configuration.class,
        BlockDirective.class
})
@Configuration
public class FreeMarkerConfiguration extends WebMvcConfigurerAdapter {

    @Resource
    freemarker.template.Configuration freemarker;

    @PostConstruct
    public void setSharedVariable(){
        freemarker.setSharedVariable("block", new BlockDirective());
        freemarker.setSharedVariable("override", new OverrideDirective());
        freemarker.setSharedVariable("extends", new ExtendsDirective());
        freemarker.setSharedVariable("timer", new TimeFormatMethod());
    }


    static class TimeFormatMethod implements TemplateMethodModelEx {

        private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

        @Override
        public Object exec(List list) throws TemplateModelException {
            if (list.size() < 1) {
                return new TemplateModelException("缺少时间秒数");
            }

            if (list.get(0) == null) {
                return "";
            }

            Integer second = Integer.valueOf(list.get(0).toString());
            String format = list.size() > 1 ? list.get(1).toString() : DEFAULT_FORMAT;
            DateFormat df = new SimpleDateFormat(format);
            df.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return df.format(new Date(second * 1000L));
        }

    }

}
