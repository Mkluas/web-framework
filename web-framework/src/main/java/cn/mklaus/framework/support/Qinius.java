package cn.mklaus.framework.support;

import cn.mklaus.framework.util.Langs;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import lombok.Data;
import org.nutz.http.Header;
import org.nutz.http.Http;
import org.nutz.http.Request;
import org.nutz.http.Sender;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author Mklaus
 * Created on 2018-01-27 下午3:07
 */
@ConditionalOnProperty(prefix = "cn.mklaus.qiniu", value = "access-key")
@EnableConfigurationProperties(Qinius.Config.class)
@Component
public class Qinius implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(Qinius.class);
    private static final String KEY = "key";
    private static final String SLASH = "/";

    private static Config config;
    private static Auth auth;
    private static Configuration configuration;
    private static BucketManager bucketManager;
    private static UploadManager uploadManager;

    public static String getBucketUrl() {
        return config.bucketUrl;
    }

    public static String getAction() {
        return config.action;
    }

    public static String getUpToken() {
        return auth.uploadToken(config.bucketName);
    }

    public static boolean delete(String key) {
        try {
            bucketManager.delete(config.bucketName, key);
            return true;
        } catch (QiniuException e) {
            logger.error("Qinius delete: file = {}, error = {}", key, e.error());
            return false;
        }
    }

    public static boolean isExists(String key) {
        String url = toLink(key);
        return Sender.create(Request.create(url, Request.METHOD.HEAD)).send().isOK();
    }

    public static String upload(File file, String key) throws QiniuException {
        Response response = uploadManager.put(file, key, getUpToken());
        if (response.isOK()) {
            return toLink(key);
        } else {
            throw new QiniuException(response);
        }
    }

    public static String toLink(String key) {
        if (key.toLowerCase().startsWith("http")) {
            return key;
        }
        return config.bucketUrl + key;
    }

    public static boolean convertToQiniu(String from, String to) {
        from = UrlSafeBase64.encodeToString(from);
        to = UrlSafeBase64.encodeToString(config.bucketName + ":" + to);
        String url = String.format("http://%s/fetch/%s/to/%s", config.fetchUrl, from, to);

        StringMap map = auth.authorization(url);
        Header header = Header.create().set("Authorization", map.get("Authorization").toString());
        String responseContent = Http.post3(url, null, header, 5000).getContent();

        JSONObject response = JSON.parseObject(responseContent);
        boolean success = Strings.isNotBlank(response.getString(KEY));
        if (!success) {
            logger.error("Qinius convert image error: {}", response.getString("error"));
        }
        return success;
    }

    public static String convertToQiniu(String url) {
        String uuid = Langs.uuid();
        if (convertToQiniu(url, uuid)) {
            return config.bucketUrl + uuid;
        } else {
            return url;
        }
    }


    @Resource
    private Config configInstance;

    @Override
    public void afterPropertiesSet() {
        Qinius.config = this.configInstance;

        Qinius.auth = Auth.create(config.accessKey, config.secretKey);
        Qinius.configuration = new Configuration(Zone.autoZone());
        Qinius.bucketManager = new BucketManager(auth, configuration);
        Qinius.uploadManager = new UploadManager(configuration);

        if (!config.getBucketUrl().endsWith(SLASH)) {
            config.setBucketUrl(config.getBucketUrl() + SLASH);
        }
        if (Strings.isBlank(config.action) || Strings.isBlank(config.fetchUrl)) {
            String content = Http.get("https://uc.qbox.me/v2/query?ak=" + config.accessKey + "&bucket=" + config.getBucketName()).getContent();
            JSONObject response = JSON.parseObject(content);
            if (Strings.isBlank(config.action)) {
                String action = response.getJSONObject("up").getJSONObject("acc").getJSONArray("main").get(0).toString();
                config.setAction(action);
            }
            if (Strings.isBlank(config.fetchUrl)) {
                String fetchUrl = response.getJSONObject("io").getJSONObject("src").getJSONArray("main").get(0).toString();
                config.setFetchUrl(fetchUrl);
            }
        }
    }

    @Data
    @ConfigurationProperties(prefix = "cn.mklaus.qiniu")
    static class Config {
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String bucketUrl;
        private String action;
        private String fetchUrl;
    }
}
