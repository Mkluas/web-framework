package cn.mklaus.framework.wechat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.wechat.pay")
public class WechatPayProperties {

    private String mchId;

    private String mchKey;

    private String keyPath = "classpath:/apiclient_cert.p12";

    public void validate() {
        Assert.hasLength(mchId, "mchId 不能为空");
        Assert.hasLength(mchKey, "mchKey 不能为空");
    }

}
