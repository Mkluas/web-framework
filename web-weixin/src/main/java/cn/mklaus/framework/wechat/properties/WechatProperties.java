package cn.mklaus.framework.wechat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.wechat")
public class WechatProperties {

}
