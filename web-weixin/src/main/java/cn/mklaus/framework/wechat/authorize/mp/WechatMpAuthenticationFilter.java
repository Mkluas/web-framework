package cn.mklaus.framework.wechat.authorize.mp;

import cn.mklaus.framework.util.Https;
import cn.mklaus.framework.util.Langs;
import cn.mklaus.framework.wechat.properties.WechatMpProperties;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Slf4j
public class WechatMpAuthenticationFilter extends AbstractAuthenticationProcessingFilter  {

    private final static String WECHAT_AUTH_CODE = "code";
    private final static String WECHAT_AUTH_STATE = "state";
    public final static String WECHAT_AUTH_STATE_VALUE = "WECHAT_AUTH_STATE";
    private final static int WECHAT_TOKEN_COOKIE_TIME = 3600 * 24 * 180;
    private final UnsuccessfulAuthenticationHandler handler;

    public WechatMpAuthenticationFilter(String pattern, AuthenticationManager manager,
                                        UnsuccessfulAuthenticationHandler handler) {
        super(new AntPathRequestMatcher(pattern));
        this.setAuthenticationManager(manager);
        this.handler = handler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException {
        String code = req.getParameter(WECHAT_AUTH_CODE);
        String state = req.getParameter(WECHAT_AUTH_STATE);

        if (StringUtils.hasLength(code) && WECHAT_AUTH_STATE_VALUE.equals(state)) {
            try {
                String token = handler.handleAuthCode(req, resp, code);
                Https.clearCookie(resp, WechatMpProperties.TOKEN_COOKIE_NAME);
                Https.setCookie(resp, WechatMpProperties.TOKEN_COOKIE_NAME, token, WECHAT_TOKEN_COOKIE_TIME, true);
                return getAuthenticationManager().authenticate(new WechatMpAuthenticationToken(token));
            } catch (WxErrorException e) {
                log.info("handleAuthCode {}", e.getError().getErrorMsg());
            }
        }

        String token = Https.getCookie(req, WechatMpProperties.TOKEN_COOKIE_NAME);
        return getAuthenticationManager().authenticate(new WechatMpAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // for debug
//        String token = Https.getCookie(request, WechatMpProperties.TOKEN_COOKIE_NAME);
//        if (Langs.IS_MAC_OS && !StringUtils.hasLength(token)) {
//            WechatMpAuthenticationToken authenticationToken = (WechatMpAuthenticationToken)authResult;
//            Https.clearCookie(response, WechatMpProperties.TOKEN_COOKIE_NAME);
//            Https.setCookie(response,
//                    WechatMpProperties.TOKEN_COOKIE_NAME,
//                    authenticationToken.getToken(),
//                    WECHAT_TOKEN_COOKIE_TIME);
//        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) {
        Https.clearCookie(resp, WechatMpProperties.TOKEN_COOKIE_NAME);
        handler.handleNoAuth(req, resp, failed);
    }

}
