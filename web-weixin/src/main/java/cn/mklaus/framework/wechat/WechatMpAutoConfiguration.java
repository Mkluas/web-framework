package cn.mklaus.framework.wechat;

import cn.mklaus.framework.wechat.authorize.mp.WechatMpJwt;
import cn.mklaus.framework.wechat.authorize.mp.WechatMpAuthConfig;
import cn.mklaus.framework.wechat.authorize.mp.*;
import cn.mklaus.framework.wechat.properties.WechatMpProperties;
import cn.mklaus.framework.wechat.service.WechatPayService;
import cn.mklaus.framework.wechat.service.WechatService;
import cn.mklaus.framework.wechat.service.WxMpUserHandler;
import cn.mklaus.framework.wechat.service.impl.DefaultWechatPayService;
import cn.mklaus.framework.wechat.service.impl.DefaultWechatService;
import cn.mklaus.framework.wechat.service.impl.DefaultWxMpUserHandler;
import cn.mklaus.framework.wechat.web.WechatController;
import cn.mklaus.framework.wechat.web.WechatMpAuthController;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

/**
 * @author Mklaus
 * Created on 2018-07-12 上午10:59
 */
@ConditionalOnProperty("cn.mklaus.wechat.mp.app-id")
@EnableConfigurationProperties(WechatMpProperties.class)
@Import({WechatMpAuthConfig.class, WechatController.class, WechatMpAuthController.class})
public class WechatMpAutoConfiguration {

    private final WechatMpProperties mp;

    public WechatMpAutoConfiguration(WechatMpProperties mp) {
        this.mp = mp;
    }

    @Bean
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl storage = new WxMpDefaultConfigImpl();
        storage.setAppId(mp.getAppId());
        storage.setSecret(mp.getSecret());
        storage.setToken(mp.getToken());
        storage.setAesKey(mp.getAesKey());
        storage.setTemplateId(mp.getTemplateId());

        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(storage);
        return wxMpService;
    }

    @Bean
    public WechatMpJwt wechatMpJwt() {
        String secret = mp.getSecret();
        if (!StringUtils.hasLength(secret)) {
            secret = mp.getAppId() + mp.getSecret();
        }
        return new WechatMpJwt(secret, mp.getJwtTimeout());
    }

    @Bean
    public WechatMpAuthenticationProvider wechatMpAuthenticationProvider(WechatMpJwt jwt) {
        return new WechatMpAuthenticationProvider(jwt, mp);
    }

    @Bean
    @ConditionalOnMissingBean(WxMpUserHandler.class)
    public WxMpUserHandler wxMpUserHandler() {
        return new DefaultWxMpUserHandler();
    }

    @Bean
    @ConditionalOnMissingBean(UnsuccessfulAuthenticationHandler.class)
    public UnsuccessfulAuthenticationHandler
    unsuccessfulAuthenticationHandler(WxMpService wxMpService, WechatMpProperties wechatMpProperties,
                                      WxMpUserHandler wxMpUserHandler, WechatMpJwt jwt) {
        return new DefaultUnsuccessfulAuthenticationHandler(wxMpService, wechatMpProperties, wxMpUserHandler, jwt);
    }

    @Bean
    @ConditionalOnMissingBean(WechatService.class)
    public WechatService wechatService() {
        return new DefaultWechatService();
    }

    @Bean
    @ConditionalOnMissingBean(WechatPayService.class)
    public WechatPayService wechatPayService() {
        return new DefaultWechatPayService();
    }

}
