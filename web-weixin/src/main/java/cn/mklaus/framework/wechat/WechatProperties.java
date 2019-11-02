package cn.mklaus.framework.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * @author Mklaus
 * Created on 2018-04-02 上午10:14
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.wechat")
public class WechatProperties {


    @Data
    @ConfigurationProperties(prefix = "cn.mklaus.wechat.pay")
    public static class Pay {

        private String mchId;

        private String mchKey;

        private String keyPath = "classpath:/apiclient_cert.p12";

        public void validate() {
            Assert.hasLength(mchId, "mchId 不能为空");
            Assert.hasLength(mchKey, "mchKey 不能为空");
        }

    }


    @Data
    @ConfigurationProperties(prefix = "cn.mklaus.wechat.mp")
    public static class Mp {

        private String appId;

        private String secret;

        private String aesKey;

        private String token;

        private String templateId;

        private String domain;

        private String notifyUri;

        public String getPayNotifyUrl() {
            Assert.hasLength(domain, "domain 不能为空");
            Assert.hasLength(notifyUri, "notifyUri 不能为空");
            return this.domain + this.notifyUri;
        }

    }

    @Data
    @ConfigurationProperties(prefix = "cn.mklaus.wechat.ma")
    public static class Ma {

        private String domain;

        private String appId;

        private String secret;

        private String msgDataFormat = "JSON";

        private String notifyUri;

        public String getPayNotifyUrl() {
            Assert.hasLength(domain, "domain 不能为空");
            Assert.hasLength(notifyUri, "notifyUri 不能为空");
            return this.domain + this.notifyUri;
        }

    }
}
