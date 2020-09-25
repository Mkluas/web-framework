package cn.mklaus.framework.wechat.authorize.mp;

import cn.mklaus.framework.wechat.authorize.WxAuthInfo;
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
 * @date 2020-08-14
 */
public class WechatMpJwt {

    private static final String OPENID = "OPENID";
    private static final String USERID = "USERID";

    private final JWTVerifier jwtVerifier;
    private final Map<String, Object> header;
    private final Algorithm algorithm;
    private final long timeout;

    public WechatMpJwt(String secret, long timeout) {
        this.timeout = timeout;
        this.algorithm = Algorithm.HMAC256(secret);
        this.jwtVerifier = JWT.require(algorithm).build();
        Map<String, Object> map = new HashMap<>(4);
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        this.header = map;
    }

    public String createToken(String openid, int userId) {
        Date expireDate = new Date(System.currentTimeMillis() + timeout);
        return JWT.create()
                .withHeader(header)
                .withClaim(OPENID, openid)
                .withClaim(USERID, userId)
                .withExpiresAt(expireDate)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public WxAuthInfo verifyToken(String token) {
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            Claim openid = jwt.getClaim(OPENID);
            Claim userid = jwt.getClaim(USERID);
            WxAuthInfo wxAuthInfo = new WxAuthInfo(openid.asString(), userid.asInt());
            wxAuthInfo.setToken(token);
            return wxAuthInfo;
        } catch (Exception e) {
            return null;
        }
    }

}
