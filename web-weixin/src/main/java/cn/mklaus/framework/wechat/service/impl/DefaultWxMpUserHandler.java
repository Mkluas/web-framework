package cn.mklaus.framework.wechat.service.impl;

import cn.mklaus.framework.wechat.service.WxMpUserHandler;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * @author klausxie
 * @date 2020-08-14
 */
@Slf4j
public class DefaultWxMpUserHandler implements WxMpUserHandler {

    @Override
    public int handleWxMpUser(WxMpUser wxMpUser) {
        log.warn("This handler should be implement by user");
        log.info(wxMpUser.toString());
        return -1;
    }

}
