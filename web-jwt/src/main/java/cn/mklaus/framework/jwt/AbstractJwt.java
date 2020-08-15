package cn.mklaus.framework.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author klausxie
 * @date 2020-08-15
 */
public abstract class AbstractJwt<T> {

    private static final String CLAIM_KEY = "clain_key";

    private final JWTVerifier jwtVerifier;
    private final Algorithm algorithm;
    private final long timeout;
    private final static Map<String, Object> HEADER;
    static {
        Map<String, Object> map = new HashMap<>(4);
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        HEADER = map;
    }

    public AbstractJwt(String secret, long timeout) {
        this.timeout = timeout;
        this.algorithm = Algorithm.HMAC256(secret);
        this.jwtVerifier = JWT.require(algorithm).build();
    }

    public String createToken(String data) {
        Date expireDate = new Date(System.currentTimeMillis() + timeout);
        return JWT.create()
                .withHeader(HEADER)
                .withClaim(CLAIM_KEY, data)
                .withExpiresAt(expireDate)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public String verifyToken(String token) {
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            Claim claim = jwt.getClaim(CLAIM_KEY);
            return claim.asString();
        } catch (Exception e) {
            return null;
        }
    }

    public abstract T getPrincipal(String token);

}
