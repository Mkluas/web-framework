package cn.mklaus.framework.wechat.authorize.ma;

import cn.mklaus.framework.jwt.AbstractJwt;
import cn.mklaus.framework.wechat.authorize.WxAuthInfo;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

/**
 * @author klausxie
 * @date 2020-08-15
 */
public class WechatMaJwt extends AbstractJwt<WxAuthInfo> {

    public WechatMaJwt(String secret, long timeout) {
        super(secret, timeout);
    }

    @Override
    public WxAuthInfo getPrincipal(String token) {
        String data = verifyToken(token);
        if (StringUtils.isEmpty(data)) {
            return null;
        }

        WxAuthInfo wxAuthInfo = JSON.parseObject(data, WxAuthInfo.class);
        wxAuthInfo.setToken(token);
        return wxAuthInfo;
    }

    public String createToken(String openid, int userId) {
        WxAuthInfo authinfo = new WxAuthInfo(openid, userId);
        return createToken(JSON.toJSONString(authinfo));
    }

}
