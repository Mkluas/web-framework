package cn.mklaus.framework.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author klausxie
 * @date 2020-07-18
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private AbstractJwt<?> jwt;
    private String token;
    private Object principal;

    public JwtAuthenticationToken(AbstractJwt<?> jwt, String token) {
        super(null);
        this.jwt = jwt;
        this.token = token;
    }

    public JwtAuthenticationToken(String token, Object principal) {
        super(null);
        this.token = token;
        this.principal = principal;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        if (principal == null) {
            this.principal = jwt.getPrincipal(this.token);
        }
        return principal;
    }

    public String getToken() {
        return token;
    }

}
