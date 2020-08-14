package cn.mklaus.framework.support.qiniu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.qiniu")
public class QiniuProperties {

    private String accessKey;

    private String secretKey;

    private String bucketName;

    private String bucketUrl;

    private String action;

    private String fetchUrl;

}
