package cn.mklaus.framework.config.exception;

import cn.mklaus.framework.bean.Response;
import cn.mklaus.framework.config.AutoConfigurationProperties;
import cn.mklaus.framework.util.Https;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mklaus
 * Created on 2018-01-03 下午6:11
 */
@ControllerAdvice
@EnableConfigurationProperties(AutoConfigurationProperties.class)
@Import({DefaultErrorController.class})
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Resource
    private AutoConfigurationProperties properties;

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ModelAndView missingServletRequestParameter(MissingServletRequestParameterException e, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        Response response = Response.error("缺少参数：" + e.getParameterName()).errCode(HttpStatus.BAD_REQUEST.value());
        return handleException(response, e, req, resp);
    }

    @ExceptionHandler(BindException.class)
    public ModelAndView bindExceptionHandler(BindException e, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        Response response = Response.error(e.getBindingResult());
        return handleException(response, e, req, resp);
    }


    @ExceptionHandler(value = TypeMismatchException.class)
    public ModelAndView requestTypeMismatch(TypeMismatchException e, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        Response response = Response
                .error(String.format("参数类型不匹配:%s.  [Cause: %s]", e.getRequiredType(), e.getCause()));
        return handleException(response, e, req, resp);
    }


    @ExceptionHandler(value = IllegalArgumentException.class)
    public ModelAndView illegalArgumentException(IllegalArgumentException e, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        return handleException(Response.error(e.getMessage()), e, req, resp);
    }


    @ExceptionHandler(value = IllegalStateException.class)
    public ModelAndView illegalStateException(IllegalStateException e, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        return handleException(Response.error(e.getMessage()), e, req, resp);
    }


    private ModelAndView handleException(Response response, Exception e, HttpServletRequest req, HttpServletResponse resp) {
        this.logError(e);
        if (Https.acceptHtml(req) && Strings.isNotBlank(properties.getErrorTemplatePath())) {
            ModelAndView mav = new ModelAndView(properties.getErrorTemplatePath());
            mav.addObject("errCode", response.getErrCode());
            mav.addObject("errMsg", response.getErrMsg());
            mav.addObject("requestUrl", req.getRequestURI());
            if (properties.isShowErrorDetail()) {
                mav.addObject("parameters", Https.extraParameter(req, "<br>"));
                mav.addObject("headers", Https.extraHeader(req, "<br>"));
                mav.addObject("cookies", Https.extraCookies(req, "<br>"));
            }
            return mav;
        } else {
            Https.response(response.build().toJSONString(), resp);
            return null;
        }
    }

    private void logError(Exception e) {
        logger.error("GlobalExceptionHandler handle exception: " + e.getMessage(), e);
    }



}
