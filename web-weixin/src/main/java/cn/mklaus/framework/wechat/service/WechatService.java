package cn.mklaus.framework.wechat.service;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author klausxie
 * @date 2020-08-14
 */
public interface WechatService {

    WxMpXmlOutMessage route(WxMpXmlMessage inMessage);

}
