package cn.mklaus.framework.wechat.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.mklaus.framework.base.BaseEntity;

/**
 * @author klausxie
 * @date 2020-09-25
 */
public interface WxMaUserHandler {

    BaseEntity handleWxMaUser(WxMaJscode2SessionResult result);

    Object me(int userId, String openid);

}
