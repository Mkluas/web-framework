package cn.mklaus.framework.exception;

import cn.mklaus.framework.ResponseProperties;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author klaus
 * Created on 2019-08-05 23:42
 */
@Slf4j
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
@EnableConfigurationProperties(ResponseProperties.class)
public class DefaultErrorController extends BasicErrorController {

    private final ResponseProperties responseProperties;

    public DefaultErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                                  ResponseProperties responseProperties) {
        super(errorAttributes, serverProperties.getError());
        this.responseProperties = responseProperties;
    }

    @Override
    @RequestMapping(produces = {"text/html"})
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = this.getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView = this.resolveErrorView(request, response, status, model);
        return modelAndView != null ? modelAndView : new ModelAndView("error", model);
    }

    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = HttpStatus.OK;
        if (responseProperties.isUseHttpStatus()) {
            status = this.getStatus(request);
        }

        Map<String, Object> body;
        if (status == HttpStatus.NO_CONTENT) {
            body = new HashMap<>();
            body.put("errCode", 500);
            body.put("errMsg", "Server Internal Error");
        } else {
            body = this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.ALL));
            body.put("errCode", status.value());
            String errMsg = body.get("error").toString();
            body.put("errMsg", errMsg);
            log.error(JSON.toJSONString(body));
        }
        return new ResponseEntity(body, status);
    }

}
