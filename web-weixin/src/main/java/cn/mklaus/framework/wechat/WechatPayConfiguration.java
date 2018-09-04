package cn.mklaus.framework.wechat;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author Mklaus
 * @date 2018-04-02 上午11:25
 */
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties({WechatProperties.Mp.class, WechatProperties.Ma.class, WechatProperties.Pay.class,})
public class WechatPayConfiguration {

    @Resource
    private WechatProperties.Ma ma;
    @Resource
    private WechatProperties.Mp mp;
    @Resource
    private WechatProperties.Pay pay;

    @Bean("wxMpPayService")
    @ConditionalOnProperty(prefix = "cn.mklaus.wechat.mp", value = "notifyUri")
    public WxPayService wxPayService() {
        WxPayConfig config = new WxPayConfig();

        config.setAppId(mp.getAppId());
        config.setNotifyUrl(mp.getPayNotifyUrl());

        config.setMchId(pay.getMchId());
        config.setMchKey(pay.getMchKey());
        config.setKeyPath(pay.getKeyPath());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(config);
        return wxPayService;
    }

    @Bean("wxMaPayService")
    @ConditionalOnProperty(prefix = "cn.mklaus.wechat.ma", value = "notifyUri")
    public WxPayService wxMaPayService() {
        WxPayConfig config = new WxPayConfig();

        config.setMchId(pay.getMchId());
        config.setMchKey(pay.getMchKey());
        config.setKeyPath(pay.getKeyPath());

        config.setAppId(ma.getAppId());
        config.setNotifyUrl(ma.getPayNotifyUrl());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(config);
        return wxPayService;
    }

}
