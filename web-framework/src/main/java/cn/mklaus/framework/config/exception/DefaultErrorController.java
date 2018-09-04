package cn.mklaus.framework.config.exception;

import cn.mklaus.framework.bean.Response;
import cn.mklaus.framework.config.AutoConfigurationProperties;
import cn.mklaus.framework.util.Https;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Mklaus
 * @date 2018-03-31 上午11:57
 */

@EnableConfigurationProperties(AutoConfigurationProperties.class)
@Controller
@RequestMapping("/error")
public class DefaultErrorController extends AbstractErrorController  {

    private final static Logger logger = LoggerFactory.getLogger(DefaultErrorController.class);

    @Resource
    private AutoConfigurationProperties properties;

    public DefaultErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return properties.getErrorTemplatePath();
    }

    public String get404Path() {
        return properties.getError404TemplatePath();
    }

    public boolean includeStackTrace() {
        return properties.isIncludeStackTrace();
    }

    public boolean showErrorDetail() {
        return properties.isShowErrorDetail();
    }

    @RequestMapping(produces = {"text/html"})
    public ModelAndView errorHtml(HttpServletRequest req, HttpServletResponse resp) {
        logger.warn("Default Error Controller handle: " + req.getRequestURI());

        HttpStatus status = this.getStatus(req);
        Map<String, Object> model = this.getErrorAttributes(req, true);
        model.put("requestUrl", req.getRequestURI());
        model.put("errCode", status.value());
        model.put("errMsg", status.getReasonPhrase());
        if (showErrorDetail()) {
            model.put("parameters", Https.extraParameter(req, "<br>"));
            model.put("headers", Https.extraHeader(req, "<br>"));
            model.put("cookies", Https.extraCookies(req, "<br>"));
        }
        resp.setStatus(status.value());

        ModelAndView mav = this.resolveErrorView(req, resp, status, model);
        String viewName = HttpStatus.NOT_FOUND.equals(status) ? get404Path() : getErrorPath();

        if (viewName == null || viewName.isEmpty()) {
            JSONObject response = Response.ok()
                    .errCode(status.value())
                    .errMsg(status.getReasonPhrase())
                    .build();
            Https.response(response.toJSONString(), resp);
            return null;
        }
        return mav == null ? new ModelAndView(viewName, model) : mav;
    }

    @RequestMapping
    @ResponseBody
    public JSONObject errorJson(HttpServletRequest request) {
        Map<String, Object> body = this.getErrorAttributes(request, includeStackTrace());
        HttpStatus status = this.getStatus(request);
        return Response.error(status.toString()).putAll(body).build();
    }

}
