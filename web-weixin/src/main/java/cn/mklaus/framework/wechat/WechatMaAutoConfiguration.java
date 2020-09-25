package cn.mklaus.framework.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.mklaus.framework.wechat.authorize.ma.WechatMaAuthConfig;
import cn.mklaus.framework.wechat.authorize.ma.WechatMaJwt;
import cn.mklaus.framework.wechat.properties.WechatMaProperties;
import cn.mklaus.framework.wechat.service.WxMaUserHandler;
import cn.mklaus.framework.wechat.service.impl.DefaultWxMaUserHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;


/**
 * @author Mklaus
 * Created on 2018-04-02 上午11:25
 */
@ConditionalOnProperty("cn.mklaus.wechat.ma.app-id")
@EnableConfigurationProperties(WechatMaProperties.class)
@Import(WechatMaAuthConfig.class)
public class WechatMaAutoConfiguration {

    private final WechatMaProperties ma;

    public WechatMaAutoConfiguration(WechatMaProperties ma) {
        this.ma = ma;
    }

    @Bean
    public WxMaService wxMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(ma.getAppId());
        config.setSecret(ma.getSecret());
        config.setMsgDataFormat(ma.getMsgDataFormat());

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

    @Bean
    @ConditionalOnMissingBean(WechatMaJwt.class)
    public WechatMaJwt wechatMaJwt() {
        String secret = ma.getJwtSecret();
        if (!StringUtils.hasLength(secret)) {
            secret = ma.getAppId() + ma.getSecret();
        }
        return new WechatMaJwt(secret, ma.getJwtTimeout());
    }

    @Bean
    @ConditionalOnMissingBean(WxMaUserHandler.class)
    public WxMaUserHandler wxMaUserHandler() {
        return new DefaultWxMaUserHandler();
    }

}
