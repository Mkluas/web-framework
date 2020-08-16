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
@ConfigurationProperties(prefix = "cn.mklaus.wechat.ma")
public class WechatMaProperties {

    private String domain;

    private String appId;

    private String secret;

    private String msgDataFormat = "JSON";

    private String notifyUri;

    private String authPathPattern;

    private List<String> passPathPatterns;

    private String jwtSecret;

    private Long jwtTimeout;

    public WechatMaProperties() {
        this.authPathPattern = "/api/ma/**";
        this.passPathPatterns = Arrays.asList("/api/ma/auth/login");
        this.jwtTimeout = 90 * (24 * 3600 * 1000L);
        this.notifyUri = "/api/wechat/pay/notify";
    }

    public String getPayNotifyUrl() {
        Assert.hasLength(domain, "domain 不能为空");
        Assert.hasLength(notifyUri, "notifyUri 不能为空");
        return this.domain + this.notifyUri;
    }

}
