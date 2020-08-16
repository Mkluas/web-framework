package cn.mklaus.framework.wechat.authorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * @author klausxie
 * @date 2020-08-16
 */
public class WxUserHolder {

    public static String getOpenid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.state(authentication.getPrincipal().getClass() == String.class, "principal not String");
        return authentication.getPrincipal().toString();
    }

}
