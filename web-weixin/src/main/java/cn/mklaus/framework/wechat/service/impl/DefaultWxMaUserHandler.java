package cn.mklaus.framework.wechat.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.mklaus.framework.base.BaseEntity;
import cn.mklaus.framework.wechat.service.WxMaUserHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author klausxie
 * @date 2020-09-25
 */
@Slf4j
public class DefaultWxMaUserHandler implements WxMaUserHandler {

    @Override
    public BaseEntity handleWxMaUser(WxMaJscode2SessionResult result) {
        log.warn("This handler method should be implement by user");
        log.info(result.toString());
        return new BaseEntity();
    }

    @Override
    public Object me(int userId, String openid) {
        log.warn("This handler method should be implement by user");
        return null;
    }
}
