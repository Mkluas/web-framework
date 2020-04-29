package cn.mklaus.framework.exception;

import cn.mklaus.framework.ResponseProperties;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author klaus
 * Created on 2019-08-05 23:42
 */
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
@EnableConfigurationProperties(ResponseProperties.class)
public class DefaultErrorController extends AbstractErrorController {

    private final Logger logger = LoggerFactory.getLogger(DefaultErrorController.class);

    private ResponseProperties responseProperties;
    private ErrorProperties errorProperties;

    public DefaultErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers,
                                  ServerProperties serverProperties, ResponseProperties responseProperties) {
        super(errorAttributes, errorViewResolvers);
        this.responseProperties = responseProperties;
        this.errorProperties = serverProperties.getError();
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }

    @RequestMapping(produces = {"text/html"})
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = this.getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(this.getErrorAttributes(request, this.isIncludeStackTrace(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView = this.resolveErrorView(request, response, status, model);
        logger.error(model.getOrDefault("message", new JSONObject(model).toJSONString()).toString());
        return modelAndView != null ? modelAndView : new ModelAndView("error", model);
    }

    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = this.getErrorAttributes(request, this.isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = responseProperties.isUseHttpStatus() ? this.getStatus(request) : HttpStatus.OK;
        body.put(responseProperties.getErrCodeKey(), 1);
        body.put(responseProperties.getErrMsgKey(), body.getOrDefault("message", "CustomErrorController: unknown err"));
        logger.error(body.getOrDefault("message", new JSONObject(body).toJSONString()).toString());
        return new ResponseEntity(body, status);
    }

    private boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        ErrorProperties.IncludeStacktrace include = this.errorProperties.getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        } else {
            return include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM && this.getTraceParameter(request);
        }
    }

}
