package cn.mklaus.framework.support.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.sendcloud")
public class SendCloudProperties {

    private String url = "http://sendcloud.sohu.com/smsapi/send";
    private String smsKey;
    private String smsUser;
    private String templateId;

    private Integer captchaLength = 4;
    private Integer timeout = 600;

}
