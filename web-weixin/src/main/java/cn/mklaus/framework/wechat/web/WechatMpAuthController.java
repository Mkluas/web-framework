package cn.mklaus.framework.wechat.web;

import cn.mklaus.framework.util.Https;
import cn.mklaus.framework.wechat.authorize.WxUserHolder;
import cn.mklaus.framework.wechat.properties.WechatMpProperties;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author klausxie
 * @date 2020-08-14
 */
@Controller
@RequestMapping
public class WechatMpAuthController {

    private final WechatMpProperties wechatMpProperties;

    public WechatMpAuthController(WechatMpProperties wechatMpProperties) {
        this.wechatMpProperties = wechatMpProperties;
    }

    @GetMapping("api/mp/auth")
    public void auth(String redirect, HttpServletResponse resp) throws IOException {
        redirect = StringUtils.hasLength(redirect) ? redirect : "/";
        Https.setCookie(resp,
                WechatMpProperties.TOKEN_COOKIE_NAME,
                WxUserHolder.getOpenid(),
                wechatMpProperties.getJwtTimeout().intValue());
        resp.sendRedirect(redirect + "#AUTH_OK");
    }

}
