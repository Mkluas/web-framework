package cn.mklaus.framework.wechat.authorize.mp;

import cn.mklaus.framework.wechat.authorize.WxAuthInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author klausxie
 * @date 2020-08-13
 */
public class WechatMpAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private WxAuthInfo principal;

    public WechatMpAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    public WechatMpAuthenticationToken(WxAuthInfo wxAuthInfo) {
        this(wxAuthInfo, null);
    }

    public WechatMpAuthenticationToken(WxAuthInfo wxAuthInfo, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = wxAuthInfo;
        this.token = wxAuthInfo.getToken();
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
