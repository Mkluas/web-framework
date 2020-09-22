package cn.mklaus.framework.wechat.authorize.ma;

import cn.mklaus.framework.jwt.AbstractJwt;
import cn.mklaus.framework.wechat.authorize.AuthInfo;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

/**
 * @author klausxie
 * @date 2020-08-15
 */
public class WechatMaJwt extends AbstractJwt<AuthInfo> {

    public WechatMaJwt(String secret, long timeout) {
        super(secret, timeout);
    }

    @Override
    public AuthInfo getPrincipal(String token) {
        String data = verifyToken(token);
        if (StringUtils.isEmpty(data)) {
            return null;
        }

        AuthInfo authInfo = JSON.parseObject(data, AuthInfo.class);
        authInfo.setToken(token);
        return authInfo;
    }

    public String createToken(String openid, int userId) {
        AuthInfo authinfo = new AuthInfo(openid, userId);
        return createToken(JSON.toJSONString(authinfo));
    }

}
