package cn.mklaus.framework.wechat;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.nutz.lang.Strings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.annotation.Resource;

/**
 * @author Mklaus
 * Created on 2018-04-02 上午11:25
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
    @Conditional({NotifyUrlCondition.class})
    public WxPayService wxMpPayService() {
        WxPayConfig config = new WxPayConfig();

        config.setAppId(mp.getAppId());
        config.setNotifyUrl(mp.getPayNotifyUrl());

        pay.validate();
        config.setMchId(pay.getMchId());
        config.setMchKey(pay.getMchKey());
        config.setKeyPath(pay.getKeyPath());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(config);
        return wxPayService;
    }

    @Bean("wxMaPayService")
    @ConditionalOnProperty(prefix = "cn.mklaus.wechat.ma", value = "domain")
    public WxPayService wxMaPayService() {
        WxPayConfig config = new WxPayConfig();

        pay.validate();
        config.setMchId(pay.getMchId());
        config.setMchKey(pay.getMchKey());
        config.setKeyPath(pay.getKeyPath());

        config.setAppId(ma.getAppId());
        config.setNotifyUrl(ma.getPayNotifyUrl());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(config);
        return wxPayService;
    }


    private static class NotifyUrlCondition implements Condition {

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            Environment environment = conditionContext.getEnvironment();
            String urlOne = environment.getProperty("cn.mklaus.wechat.mp.notify-uri");
            String urlTwo = environment.getProperty("cn.mklaus.wechat.mp.notifyUri");
            if (Strings.isNotBlank(urlOne) || Strings.isNotBlank(urlTwo)) {
                return true;
            }
            return false;
        }

    }

}
