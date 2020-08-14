package cn.mklaus.framework.wechat.web;

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

    @GetMapping("api/mp/auth")
    public void auth(String redirect, HttpServletResponse resp) throws IOException {
        redirect = StringUtils.hasLength(redirect) ? redirect : "/";
        resp.sendRedirect(redirect + "#AUTH_OK");
    }

}
