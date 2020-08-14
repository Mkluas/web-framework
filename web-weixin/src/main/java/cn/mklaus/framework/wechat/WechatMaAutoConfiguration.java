package cn.mklaus.framework.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.mklaus.framework.wechat.properties.WechatMaProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * @author Mklaus
 * Created on 2018-04-02 上午11:25
 */
@ConditionalOnProperty("cn.mklaus.wechat.ma.app-id")
@EnableConfigurationProperties(WechatMaProperties.class)
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

}
