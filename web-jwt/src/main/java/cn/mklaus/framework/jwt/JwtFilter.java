package cn.mklaus.framework.jwt;

import cn.mklaus.framework.util.Https;
import cn.mklaus.framework.web.Response;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author klausxie
 * @date 2020-07-18
 */
public class JwtFilter extends AbstractAuthenticationProcessingFilter  {

    private TokenExtractor tokenExtractor;

    public JwtFilter(String pattern, AuthenticationManager manager, TokenExtractor tokenExtractor) {
        super(new AntPathRequestMatcher(pattern));
        this.setAuthenticationManager(manager);
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException, IOException, ServletException {
        JwtAuthenticationToken authenticationToken = this.tokenExtractor.extract(req);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) throws IOException, ServletException {
        Response response = Response.error(failed.getMessage()).errCode(401);
        Https.response(response.build().toJSONString(), resp);
    }

    public interface TokenExtractor {
        /**
         * 提取token
         * @param req HttpServletRequest
         * @return
         */
        JwtAuthenticationToken extract(HttpServletRequest req);
    }

}
