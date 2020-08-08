package cn.mklaus.framework.config.secure;

import cn.mklaus.framework.util.Https;
import cn.mklaus.framework.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author klausxie
 * @date 2020-08-08
 */
@Slf4j
@Configuration
@EnableWebSecurity
@Import({AdminAuthenticationProvider.class, AdminUserDetailsService.class})
public class AdminSecureConfig extends WebSecurityConfigurerAdapter {

    private final AdminAuthenticationProvider adminAuthenticationProvider;
    private final AdminUserDetailsService adminUserDetailsService;

    public AdminSecureConfig(AdminAuthenticationProvider adminAuthenticationProvider,
                             AdminUserDetailsService adminUserDetailsService) {
        this.adminAuthenticationProvider = adminAuthenticationProvider;
        this.adminUserDetailsService = adminUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationHandler handler = new AuthenticationHandler();
        AdminAntRequestMatcher adminAntRequestMatcher = new AdminAntRequestMatcher();

        http.requestMatcher(adminAntRequestMatcher)
                .csrf().disable()
                .authenticationProvider(this.adminAuthenticationProvider)

                .formLogin()
                .usernameParameter("account")
                .loginProcessingUrl("/api/auth/login")
                .failureHandler(handler)
                .successHandler(handler)

                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(handler)
                .permitAll()

                .and()
                .rememberMe()
                .userDetailsService(this.adminUserDetailsService)
                .tokenValiditySeconds(30 * 24 * 3600)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(handler)
                .accessDeniedHandler(handler)

                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/login").permitAll()
                .anyRequest().authenticated();
    }

    public static class AdminAntRequestMatcher implements RequestMatcher {

        private AntPathRequestMatcher backendMatcher;
        private AntPathRequestMatcher authMatcher;

        public AdminAntRequestMatcher() {
            this.authMatcher = new AntPathRequestMatcher("/api/auth/**");
            this.backendMatcher = new AntPathRequestMatcher("/api/backend/**");
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            return authMatcher.matches(request) || backendMatcher.matches(request);
        }
    }

    private static class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler,
            AuthenticationEntryPoint, LogoutSuccessHandler, AccessDeniedHandler {
        @Override
        public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
            Response response = Response.error("没有登录").errCode(401);
            Https.response(response.build().toJSONString(), resp);
        }

        @Override
        public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException e) throws IOException, ServletException {
            Response response = Response.error("没有权限").errCode(403);
            Https.response(response.build().toJSONString(), resp);
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
            Response response = Response.error(e.getMessage());
            Https.response(response.build().toJSONString(), resp);
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
            Response response = Response.ok().errMsg("登录成功");
            Https.response(response.build().toJSONString(), resp);
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
            Response response = Response.ok().errMsg("登出成功");
            Https.response(response.build().toJSONString(), resp);
        }

    }

}
