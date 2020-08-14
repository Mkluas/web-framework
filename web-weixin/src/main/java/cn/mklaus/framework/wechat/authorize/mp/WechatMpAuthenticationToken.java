package cn.mklaus.framework.wechat.authorize.mp;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author klausxie
 * @date 2020-08-13
 */
public class WechatMpAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private String openid;

    public WechatMpAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    public WechatMpAuthenticationToken(String token, String openid) {
        this(token, openid, null);
    }

    public WechatMpAuthenticationToken(String token, String openid, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.openid = openid;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return openid;
    }

    public String getToken() {
        return token;
    }

    public String getOpenid() {
        return openid;
    }

}
