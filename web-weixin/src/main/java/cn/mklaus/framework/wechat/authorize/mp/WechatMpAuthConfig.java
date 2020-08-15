package cn.mklaus.framework.wechat.authorize.mp;

import cn.mklaus.framework.wechat.properties.WechatMpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Slf4j
@Configuration
@EnableWebSecurity
@Order(Integer.MIN_VALUE + 3)
public class WechatMpAuthConfig extends WebSecurityConfigurerAdapter {

    private final WechatMpAuthenticationProvider provider;
    private final UnsuccessfulAuthenticationHandler handler;
    private final WechatMpProperties mpProperties;

    public WechatMpAuthConfig(WechatMpAuthenticationProvider provider, WechatMpProperties mpProperties,
                              UnsuccessfulAuthenticationHandler handler) {
        this.provider = provider;
        this.mpProperties = mpProperties;
        this.handler = handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(mpProperties.getAuthPathPattern())
                .csrf().disable()
                .authenticationProvider(provider)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.apply(new SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>() {
            @Override
            public void init(HttpSecurity httpSecurity) throws Exception {
            }
            @Override
            public void configure(HttpSecurity httpSecurity) throws Exception {
                AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
                WechatMpAuthenticationFilter filter = new WechatMpAuthenticationFilter(
                        mpProperties.getAuthPathPattern(), authenticationManager, handler);
                httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        if (mpProperties.getPassPathPatterns() != null) {
            mpProperties.getPassPathPatterns().forEach(path -> web.ignoring().mvcMatchers(path));
        }
    }

}
