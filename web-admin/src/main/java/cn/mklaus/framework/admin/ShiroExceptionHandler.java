package cn.mklaus.framework.admin;

import cn.mklaus.framework.bean.BaseErrorEnum;
import cn.mklaus.framework.bean.Response;
import cn.mklaus.framework.config.AutoConfigurationProperties;
import cn.mklaus.framework.util.Https;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mklaus
 * Created on 2018-03-31 下午12:07
 */
@ConditionalOnClass({AuthenticationException.class, UnauthenticatedException.class})
@ControllerAdvice
@EnableConfigurationProperties(AutoConfigurationProperties.class)
@Order(1)
public class ShiroExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ShiroExceptionHandler.class);

    @Resource
    private AutoConfigurationProperties properties;

    @ExceptionHandler(value = UnauthenticatedException.class)
    public ModelAndView unauthenticatedExceptionHandler(UnauthenticatedException e, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        return handleException(Response.error(BaseErrorEnum.ACCOUNT_UNAUTHENTICATED), e, req, resp);
    }

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public ModelAndView incorrectCredentialsExceptionHandler(IncorrectCredentialsException e, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        return handleException(Response.error(BaseErrorEnum.WRONG_ACCOUNT_OR_PASSWORD), e, req, resp);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ModelAndView authenticationExceptionHandler(AuthenticationException e, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        return handleException(Response.error(BaseErrorEnum.WRONG_ACCOUNT_OR_PASSWORD), e, req, resp);
    }

    @ExceptionHandler(value = DisabledAccountException.class)
    public ModelAndView disabledAccountExceptionHandler(DisabledAccountException e, HttpServletRequest req, HttpServletResponse resp) {
        return handleException(Response.error(BaseErrorEnum.ACCOUNT_DISABLED), e, req, resp);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ModelAndView unauthorizedExceptionHandler(UnauthorizedException e, HttpServletRequest req, HttpServletResponse resp) {
        return handleException(Response.error(BaseErrorEnum.PERMISSION_DENIED), e, req, resp);
    }

    private ModelAndView handleException(Response response, Exception e, HttpServletRequest req, HttpServletResponse resp) {
        this.logError(e);
        if (Https.acceptHtml(req) && Strings.isNotBlank(properties.getErrorTemplatePath())) {
            ModelAndView mav = new ModelAndView(properties.getErrorTemplatePath());
            mav.addObject("errCode", response.getErrCode());
            mav.addObject("errMsg", response.getErrMsg());
            mav.addObject("requestUrl", req.getRequestURI());
            return mav;
        } else {
            Https.response(response.build().toJSONString(), resp);
            return null;
        }
    }

    private void logError(Exception e) {
        logger.error("ShiroExceptionHandler handle exception: " + e.getMessage(), e);
    }


}
