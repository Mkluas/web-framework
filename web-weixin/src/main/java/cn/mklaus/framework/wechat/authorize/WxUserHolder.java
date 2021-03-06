package cn.mklaus.framework.wechat.authorize;

import cn.mklaus.framework.jwt.JwtAuthenticationToken;
import cn.mklaus.framework.wechat.authorize.mp.WechatMpAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * @author klausxie
 * @date 2020-08-16
 */
public class WxUserHolder {

    public static int getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.state(authentication.getPrincipal().getClass() == WxAuthInfo.class, "principal not AuthInfo");
        WxAuthInfo wxAuthInfo = (WxAuthInfo) authentication.getPrincipal();
        return wxAuthInfo.getUserId();
    }

    public static String getOpenid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.state(authentication.getPrincipal().getClass() == WxAuthInfo.class, "principal not AuthInfo");
        WxAuthInfo wxAuthInfo = (WxAuthInfo) authentication.getPrincipal();
        return wxAuthInfo.getOpenid();
    }

    public static String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        }
        if (authentication instanceof WechatMpAuthenticationToken) {
            return ((WechatMpAuthenticationToken) authentication).getToken();
        }
        throw new IllegalStateException("Authentication do not contain token");
    }

}
