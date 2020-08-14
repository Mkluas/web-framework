package cn.mklaus.framework.wechat.service.impl;

import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.wechat.service.WechatPayService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author klausxie
 * @date 2020-08-14
 */
@Slf4j
public class DefaultWechatPayService implements WechatPayService {

    @Override
    public ServiceResult handlePayNotify(String data) {
        log.warn("This service should be implement by user");
        log.info("Receive pay notify: {}", data);
        return ServiceResult.ok();
    }

}
