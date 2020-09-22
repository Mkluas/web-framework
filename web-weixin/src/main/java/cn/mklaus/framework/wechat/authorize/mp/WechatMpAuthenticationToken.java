package cn.mklaus.framework.wechat.authorize.mp;

import cn.mklaus.framework.wechat.authorize.AuthInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author klausxie
 * @date 2020-08-13
 */
public class WechatMpAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private AuthInfo principal;

    public WechatMpAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    public WechatMpAuthenticationToken(AuthInfo authInfo) {
        this(authInfo, null);
    }

    public WechatMpAuthenticationToken(AuthInfo authInfo, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = authInfo;
        this.token = authInfo.getToken();
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getToken() {
        return token;
    }

}
