package cn.mklaus.framework.wechat.authorize.mp;

import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author klausxie
 * @date 2020-08-14
 */
public interface UnsuccessfulAuthenticationHandler {

    String handleAuthCode(HttpServletRequest req, HttpServletResponse resp, String authCode) throws WxErrorException;

    void handleNoAuth(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed);

}
