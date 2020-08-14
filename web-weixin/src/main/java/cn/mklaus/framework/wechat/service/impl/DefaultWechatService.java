package cn.mklaus.framework.wechat.service.impl;

import cn.mklaus.framework.wechat.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author klausxie
 * @date 2020-08-14
 */
@Slf4j
public class DefaultWechatService implements WechatService {

    @Override
    public WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
        log.warn("This service should be implement by user");
        log.info("Receive: {}", inMessage);
        return null;
    }

}
