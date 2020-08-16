package cn.mklaus.framework.web;

import cn.mklaus.framework.AutoConfigurationProperties;
import com.alibaba.fastjson.JSONArray;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.Locale;

/**
 * @author klaus
 * Created on 2019-08-06 18:09
 */
@Configuration
@EnableConfigurationProperties(AutoConfigurationProperties.class)
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    private final AutoConfigurationProperties properties;

    public CustomWebMvcConfigurer(AutoConfigurationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (properties.isAllowCors()) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                    .allowCredentials(true).maxAge(3600);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<JSONArray>() {
            @Override
            public JSONArray parse(String s, Locale locale) {
                return JSONArray.parseArray(s);
            }
            @Override
            public String print(JSONArray objects, Locale locale) {
                return objects.toJSONString();
            }
        });
    }

}
