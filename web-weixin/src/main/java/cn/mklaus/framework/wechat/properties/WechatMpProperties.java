package cn.mklaus.framework.wechat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.wechat.mp")
public class WechatMpProperties {

    private String appId;

    private String secret;

    private String aesKey;

    private String token;

    private String templateId;

    private String domain;

    private String notifyUri;

    private String authPathPattern;

    private List<String> passPathPatterns;

    private String jwtSecret;

    private Long jwtTimeout;

    private String openid;

    public WechatMpProperties() {
        this.authPathPattern = "/api/mp/**";
        this.passPathPatterns = Arrays.asList("/api/wechat/**");
        this.jwtTimeout = 90 * (24 * 3600 * 1000L);
        this.notifyUri = "/api/wechat/pay/notify";
    }

    public String getPayNotifyUrl() {
        Assert.hasLength(domain, "domain 不能为空");
        Assert.hasLength(notifyUri, "notifyUri 不能为空");
        return this.domain + this.notifyUri;
    }

}
