package cn.mklaus.framework.jwt;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author klausxie
 * @date 2020-07-18
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        String token = authenticationToken.getToken();

        if (Strings.isBlank(authenticationToken.getToken())) {
            throw new InsufficientAuthenticationException("token not found");
        }

        Object principal = authenticationToken.getPrincipal();
        if (principal != null) {
            return new JwtAuthenticationToken(token, principal);
        }

        throw new BadCredentialsException("token expire or invalid");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtAuthenticationToken.class.isAssignableFrom(aClass);
    }

}
