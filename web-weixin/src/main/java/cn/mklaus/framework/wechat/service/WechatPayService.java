package cn.mklaus.framework.wechat.service;

import cn.mklaus.framework.bean.ServiceResult;

/**
 * @author klausxie
 * @date 2020-08-14
 */
public interface WechatPayService {

    ServiceResult handlePayNotify(String data);

}
