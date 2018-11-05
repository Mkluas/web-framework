package cn.mklaus.framework.wechat;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author Mklaus
 * Created on 2018-07-12 上午10:59
 */
@ConditionalOnClass(WxMpService.class)
@EnableConfigurationProperties(WechatProperties.Mp.class)
public class WechatMpConfiguration {

    @Resource
    private WechatProperties.Mp mp;

    @Bean
    public WxMpService wxMpService() {
        WxMpInMemoryConfigStorage storage = new WxMpInMemoryConfigStorage();
        storage.setAppId(mp.getAppId());
        storage.setSecret(mp.getSecret());
        storage.setToken(mp.getToken());
        storage.setAesKey(mp.getAesKey());
        storage.setTemplateId(mp.getTemplateId());

        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(storage);
        return wxMpService;
    }

}
