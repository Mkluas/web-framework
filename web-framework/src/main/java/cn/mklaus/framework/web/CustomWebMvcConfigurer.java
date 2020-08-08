package cn.mklaus.framework.web;

import com.alibaba.fastjson.JSONArray;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author klaus
 * Created on 2019-08-06 18:09
 */
@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<JSONArray>() {
            @Override
            public JSONArray parse(String s, Locale locale) throws ParseException {
                return JSONArray.parseArray(s);
            }
            @Override
            public String print(JSONArray objects, Locale locale) {
                return objects.toJSONString();
            }
        });
    }

}
