package cn.mklaus.framework.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author Mklaus
 * Created on 2018-04-02 上午11:25
 */
@ConditionalOnClass(WxMaService.class)
@EnableConfigurationProperties(WechatProperties.Ma.class)
public class WechatMaConfiguration {

    @Resource
    private WechatProperties.Ma ma;

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
