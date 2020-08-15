package cn.mklaus.framework.wechat.authorize.ma;

import cn.mklaus.framework.jwt.AbstractJwtSecurityConfig;
import cn.mklaus.framework.wechat.properties.WechatMaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author klausxie
 * @date 2020-08-14
 */
@Slf4j
@Configuration
@EnableWebSecurity
@Order(Integer.MIN_VALUE + 4)
public class WechatMaAuthConfig extends AbstractJwtSecurityConfig {

    private WechatMaProperties maProperties;
    private WechatMaJwt wechatMaJwt;

    public WechatMaAuthConfig(WechatMaProperties maProperties, WechatMaJwt wechatMaJwt) {
        this.maProperties = maProperties;
        this.wechatMaJwt = wechatMaJwt;
    }

    @Override
    protected String matchPattern() {
        return maProperties.getAuthPathPattern();
    }

    @Override
    protected String extractToken(HttpServletRequest req) {
        return req.getParameter("token");
    }

    @Override
    protected WechatMaJwt jwt() {
        return wechatMaJwt;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        if (maProperties.getPassPathPatterns() != null) {
            maProperties.getPassPathPatterns().forEach(path -> web.ignoring().mvcMatchers(path));
        }
    }


}
