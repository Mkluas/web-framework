package cn.mklaus.framework.wechat.authorize.ma;

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

/**
 * @author klausxie
 * @date 2020-08-14
 */
@Slf4j
@Configuration
@EnableWebSecurity
@Order(Integer.MIN_VALUE + 4)
public class WechatMaAuthConfig extends WebSecurityConfigurerAdapter {

    private final static String MATCH_PATH = "/api/ma/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(MATCH_PATH)
                .csrf().disable()
//                .authenticationProvider(provider)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.apply(new SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>() {
            @Override
            public void init(HttpSecurity httpSecurity) throws Exception {
            }
            @Override
            public void configure(HttpSecurity httpSecurity) throws Exception {
                AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/api/ma/auth/**");
    }

}
