package cn.mklaus.framework.wechat.authorize.mp;

import cn.mklaus.framework.exception.BaseErrorEnum;
import cn.mklaus.framework.util.Https;
import cn.mklaus.framework.web.Response;
import cn.mklaus.framework.wechat.properties.WechatMpProperties;
import cn.mklaus.framework.wechat.service.WxMpUserHandler;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author klausxie
 * @date 2020-08-14
 */
public class DefaultUnsuccessfulAuthenticationHandler implements UnsuccessfulAuthenticationHandler {

    private final WxMpService wxMpService;
    private final WechatMpProperties wechatMpProperties;
    private final WxMpUserHandler wxMpUserHandler;
    private final WechatMpJwt wechatMpJwt;

    public DefaultUnsuccessfulAuthenticationHandler(WxMpService wxMpService, WechatMpProperties wechatMpProperties,
                                                    WxMpUserHandler wxMpUserHandler, WechatMpJwt wechatMpJwt) {
        this.wxMpService = wxMpService;
        this.wechatMpProperties = wechatMpProperties;
        this.wxMpUserHandler = wxMpUserHandler;
        this.wechatMpJwt = wechatMpJwt;
    }

    @Override
    public String handleAuthCode(HttpServletRequest req, HttpServletResponse resp, String authCode) throws WxErrorException {
        WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(authCode);
        WxMpUser mpUser = wxMpService.oauth2getUserInfo(accessToken, "zh_CN");
        int userId = wxMpUserHandler.handleWxMpUser(mpUser);
        return wechatMpJwt.createToken(mpUser.getOpenId(), userId);
    }

    @SneakyThrows
    @Override
    public void handleNoAuth(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) {
        if (Https.acceptHtml(req)) {
            String queryString = StringUtils.hasLength(req.getQueryString()) ? "?" + req.getQueryString() : "";
            String currentUrl = wechatMpProperties.getDomain() + req.getRequestURI() + queryString;
            String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(
                    currentUrl,
                    WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                    WechatMpAuthenticationFilter.WECHAT_AUTH_STATE_VALUE
            );
            resp.sendRedirect(redirectUrl);
        } else {
            Response response = Response.error(BaseErrorEnum.ACCOUNT_UNAUTHENTICATED);
            Https.response(response.toString(), resp);
        }
    }

}
