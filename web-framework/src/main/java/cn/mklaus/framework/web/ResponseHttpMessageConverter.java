package cn.mklaus.framework.web;

import cn.mklaus.framework.ResponseProperties;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author klaus
 * Created on 2019-08-06 22:24
 */
public class ResponseHttpMessageConverter implements HttpMessageConverter<Response> {

    private final ResponseProperties responseProperties;
    private final FastJsonHttpMessageConverter fastJsonHttpMessageConverter;

    public ResponseHttpMessageConverter(FastJsonHttpMessageConverter converter, ResponseProperties responseProperties) {
        this.responseProperties = responseProperties;
        this.fastJsonHttpMessageConverter = converter;
    }

    @Override
    public boolean canRead(Class<?> aClass, MediaType mediaType) {
        return Response.class == aClass;
    }

    @Override
    public boolean canWrite(Class<?> aClass, MediaType mediaType) {
        return Response.class == aClass;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.ALL, MediaType.APPLICATION_JSON);
    }

    @Override
    public Response read(Class<? extends Response> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        throw new IllegalStateException("Never be invoked");
    }

    @Override
    public void write(Response response, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        this.fastJsonHttpMessageConverter.write(response.build(this.responseProperties), mediaType, httpOutputMessage);
    }

}
