package cn.mklaus.framework.wechat.authorize.ma;

import cn.mklaus.framework.jwt.AbstractJwt;

/**
 * @author klausxie
 * @date 2020-08-15
 */
public class WechatMaJwt extends AbstractJwt<String> {

    public WechatMaJwt(String secret, long timeout) {
        super(secret, timeout);
    }

    @Override
    public String getPrincipal(String token) {
        return verifyToken(token);
    }
}
