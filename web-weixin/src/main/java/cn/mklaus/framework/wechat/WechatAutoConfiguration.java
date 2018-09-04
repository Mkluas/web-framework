package cn.mklaus.framework.wechat;

import org.springframework.context.annotation.Import;

/**
 * @author Mklaus
 * @date 2018-04-02 上午10:34
 */
@Import({WechatMpConfiguration.class, WechatMaConfiguration.class, WechatPayConfiguration.class})
public class WechatAutoConfiguration {

}
