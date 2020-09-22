package cn.mklaus.framework.wechat.authorize.mp;

import cn.mklaus.framework.util.Langs;
import cn.mklaus.framework.wechat.authorize.AuthInfo;
import cn.mklaus.framework.wechat.properties.WechatMpProperties;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

/**
 * @author klausxie
 * @date 2020-08-13
 */
public class WechatMpAuthenticationProvider implements AuthenticationProvider {

    private final WechatMpJwt jwt;
    private final WechatMpProperties wechatMpProperties;

    public WechatMpAuthenticationProvider(WechatMpJwt jwt, WechatMpProperties wechatMpProperties) {
        this.jwt = jwt;
        this.wechatMpProperties = wechatMpProperties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatMpAuthenticationToken wechatToken = (WechatMpAuthenticationToken) authentication;

        if (Langs.IS_MAC_OS && StringUtils.hasLength(wechatMpProperties.getOpenid())) {
            String token = jwt.createToken(wechatMpProperties.getOpenid(), wechatMpProperties.getUserId());
            AuthInfo authInfo = new AuthInfo(wechatMpProperties.getOpenid(), wechatMpProperties.getUserId());
            authInfo.setToken(token);
            return new WechatMpAuthenticationToken(authInfo);
        }

        if (!StringUtils.hasLength(wechatToken.getToken())) {
            throw new BadCredentialsException("token not found");
        }

        AuthInfo authInfo = jwt.verifyToken(wechatToken.getToken());
        if (authInfo != null) {
            return new WechatMpAuthenticationToken(authInfo);
        }

        throw new BadCredentialsException("token expire or invalid");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == WechatMpAuthenticationToken.class;
    }

}
