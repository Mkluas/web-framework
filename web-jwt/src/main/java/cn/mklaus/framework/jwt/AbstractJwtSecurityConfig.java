package cn.mklaus.framework.jwt;

import cn.mklaus.framework.util.Https;
import cn.mklaus.framework.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author klausxie
 * @date 2020-07-23
 */
@Slf4j
@Configuration
@Order(Integer.MIN_VALUE + 20)
public abstract class AbstractJwtSecurityConfig<T> extends WebSecurityConfigurerAdapter
        implements AccessDeniedHandler {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(matchPattern())
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(new JwtAuthenticationProvider())
                .exceptionHandling()
                .accessDeniedHandler(this);

        http.apply(new SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>() {
            @Override
            public void init(HttpSecurity builder) throws Exception {}
            @Override
            public void configure(HttpSecurity builder) throws Exception {
                AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
                JwtFilter filter = new JwtFilter(matchPattern(), authenticationManager, req -> {
                    String token = extractToken(req);
                    return new JwtAuthenticationToken(jwt(), token);
                });
                builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
            }
        });
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException e) {
        Response response = Response.error("没有权限").errCode(403);
        Https.response(response.build().toJSONString(), resp);
    }

    protected abstract String matchPattern();

    protected abstract String extractToken(HttpServletRequest req);

    protected abstract AbstractJwt<T> jwt();

}
