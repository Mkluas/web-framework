package cn.mklaus.framework.web;

import cn.mklaus.framework.ResponseProperties;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * @author klaus
 * @date 2019-08-07 16:23
 */
@Configuration
@Import(CustomWebMvcConfigurer.class)
@EnableConfigurationProperties(ResponseProperties.class)
public class WebAutoConfiguration {

    private ResponseProperties responseProperties;

    public WebAutoConfiguration(ResponseProperties responseProperties) {
        this.responseProperties = responseProperties;
    }

    @Bean
    @ConditionalOnMissingBean(value = FastJsonHttpMessageConverter.class)
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                WriteNullNumberAsZero,
                WriteNullListAsEmpty,
                WriteNullBooleanAsFalse,
                SerializerFeature.PrettyFormat);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(fastMediaTypes);
        converter.setFastJsonConfig(fastJsonConfig);
        return converter;
    }

    @Bean
    @ConditionalOnBean(FastJsonHttpMessageConverter.class)
    @Order(1)
    public ResponseHttpMessageConverter responseHttpMessageConverter(FastJsonHttpMessageConverter fastJsonHttpMessageConverter) {
        return new ResponseHttpMessageConverter(fastJsonHttpMessageConverter, this.responseProperties);
    }

}
