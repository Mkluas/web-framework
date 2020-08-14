package cn.mklaus.framework.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.mklaus.framework.wechat.properties.WechatMaProperties;
import cn.mklaus.framework.wechat.properties.WechatMpProperties;
import cn.mklaus.framework.wechat.properties.WechatPayProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Mklaus
 * Created on 2018-04-02 上午11:25
 */
@ConditionalOnProperty(value = "cn.mklaus.wechat.pay.mch-id")
@EnableConfigurationProperties({WechatMpProperties.class, WechatMaProperties.class, WechatPayProperties.class,})
public class WechatPayAutoConfiguration {

    private final WechatMaProperties ma;
    private final WechatMpProperties mp;
    private final WechatPayProperties pay;

    public WechatPayAutoConfiguration(WechatMaProperties ma, WechatMpProperties mp, WechatPayProperties pay) {
        this.ma = ma;
        this.mp = mp;
        this.pay = pay;
    }

    @Bean("wxMpPayService")
    @ConditionalOnBean(WxMpService.class)
    public WxPayService wxMpPayService() {
        WxPayConfig config = createWxPayConfig();
        config.setAppId(mp.getAppId());
        config.setNotifyUrl(mp.getPayNotifyUrl());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(config);
        return wxPayService;
    }

    @Bean("wxMaPayService")
    @ConditionalOnBean(WxMaService.class)
    public WxPayService wxMaPayService() {
        WxPayConfig config = createWxPayConfig();
        config.setAppId(ma.getAppId());
        config.setNotifyUrl(ma.getPayNotifyUrl());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(config);
        return wxPayService;
    }

    private WxPayConfig createWxPayConfig() {
        WxPayConfig config = new WxPayConfig();

        pay.validate();
        config.setMchId(pay.getMchId());
        config.setMchKey(pay.getMchKey());
        config.setKeyPath(pay.getKeyPath());

        return config;
    }

}
