package cn.mklaus.framework.wechat.service;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * @author klausxie
 * @date 2020-08-14
 */
public interface WxMpUserHandler {

    int handleWxMpUser(WxMpUser wxMpUser);

}
