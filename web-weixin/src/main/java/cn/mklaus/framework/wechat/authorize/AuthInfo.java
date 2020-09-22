package cn.mklaus.framework.wechat.authorize;

import lombok.Data;

/**
 * @author klausxie
 * @date 2020-09-22
 */
@Data
public class AuthInfo {
    private String openid;
    private Integer userId;
    private String token;
    public AuthInfo(String openid, Integer userId) {
        this.openid = openid;
        this.userId = userId;
    }
}
