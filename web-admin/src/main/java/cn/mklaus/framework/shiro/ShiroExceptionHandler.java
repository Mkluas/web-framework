package cn.mklaus.framework.shiro;

import cn.mklaus.framework.exception.BaseErrorEnum;
import cn.mklaus.framework.web.Response;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Mklaus
 * Created on 2018-03-31 下午12:07
 */
@ConditionalOnClass({AuthenticationException.class, UnauthenticatedException.class})
@RestControllerAdvice
@Order(1)
public class ShiroExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(cn.mklaus.framework.shiro.ShiroExceptionHandler.class);


    @ExceptionHandler(value = UnauthenticatedException.class)
    public Response unauthenticatedExceptionHandler(UnauthenticatedException e) {
        return internalHandler(BaseErrorEnum.ACCOUNT_UNAUTHENTICATED, e);
    }

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public Response incorrectCredentialsExceptionHandler(IncorrectCredentialsException e) {
        return internalHandler(BaseErrorEnum.WRONG_ACCOUNT_OR_PASSWORD, e);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public Response authenticationExceptionHandler(AuthenticationException e) {
        return internalHandler(BaseErrorEnum.WRONG_ACCOUNT_OR_PASSWORD, e);
    }

    @ExceptionHandler(value = DisabledAccountException.class)
    public Response disabledAccountExceptionHandler(DisabledAccountException e) {
        return internalHandler(BaseErrorEnum.ACCOUNT_DISABLED, e);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public Response unauthorizedExceptionHandler(UnauthorizedException e) {
        return internalHandler(BaseErrorEnum.PERMISSION_DENIED, e);
    }

    private Response internalHandler(BaseErrorEnum errorEnum, Exception e) {
        logException(e);
        return Response.error(errorEnum);
    }

    private void logException(Exception e) {
        logger.error("ShiroExceptionHandler: " + e.getMessage());
    }


}
