package cn.mklaus.framework.support.qiniu;

import cn.mklaus.framework.support.web.QiniuController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import lombok.extern.slf4j.Slf4j;
import org.nutz.http.Header;
import org.nutz.http.Http;
import org.nutz.lang.Strings;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Slf4j
@Component
@EnableConfigurationProperties(QiniuProperties.class)
@ConditionalOnProperty(value = "cn.mklaus.qiniu.access-key")
@Import(QiniuController.class)
public class Qiniu implements ApplicationRunner {

    private static final String KEY = "key";
    private static final String SLASH = "/";

    private final Auth auth;
    private final Configuration configuration;
    private final BucketManager bucketManager;
    private final UploadManager uploadManager;

    private final QiniuProperties qiniuProperties;

    public Qiniu(QiniuProperties qiniuProperties) {
        this.qiniuProperties = qiniuProperties;
        this.auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        this.configuration = new Configuration(Zone.autoZone());
        this.bucketManager = new BucketManager(auth, configuration);
        this.uploadManager = new UploadManager(configuration);
    }

    public QiniuProperties getQiniuProperties() {
        return qiniuProperties;
    }

    public String getUploadToken() {
        return auth.uploadToken(qiniuProperties.getBucketName());
    }

    public boolean delete(String key) {
        try {
            bucketManager.delete(qiniuProperties.getBucketName(), key);
            return true;
        } catch (QiniuException e) {
            log.error("Qiniu delete {}, error = {}", key, e.error());
            return false;
        }
    }

    public TransferResult upload(File file, String targetKey) throws QiniuException {
        Response response = uploadManager.put(file, targetKey, getUploadToken());
        if (response.isOK()) {
            String targetUrl = qiniuProperties.getBucketUrl() + targetKey;
            return TransferResult.ok(targetUrl);
        } else {
            return TransferResult.error(response.error);
        }
    }

    public TransferResult transfer(String source, String targetKey) {
        source = UrlSafeBase64.encodeToString(source);
        targetKey = UrlSafeBase64.encodeToString(qiniuProperties.getBucketName()+":"+targetKey);
        String url = String.format("http://%s/fetch/%s/to/%s", qiniuProperties.getFetchUrl(), source, targetKey);

        String authorization = auth.authorization(url).get("Authorization").toString();
        Header header = Header.create().set("Authorization", authorization);
        String responseContent = Http.post3(url, null, header, 5000).getContent();
        JSONObject response = JSON.parseObject(responseContent);

        if (Strings.isNotBlank(response.getString(KEY))) {
            String targetUrl = qiniuProperties.getBucketUrl() + response.getString(KEY);
            return TransferResult.ok(targetUrl);
        } else {
            return TransferResult.error(response.getString("error"));
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!qiniuProperties.getBucketUrl().endsWith(SLASH)) {
            qiniuProperties.setBucketUrl(qiniuProperties.getBucketUrl() + SLASH);
        }

        String action = qiniuProperties.getAction();
        String fetchUrl = qiniuProperties.getFetchUrl();
        String accessKey = qiniuProperties.getAccessKey();
        String bucketName = qiniuProperties.getBucketName();
        if (Strings.isBlank(action) || Strings.isBlank(fetchUrl)) {
            String url = "https://uc.qbox.me/v2/query?ak=" + accessKey + "&bucket=" + bucketName;
            String content = Http.get(url).getContent();
            JSONObject response = JSON.parseObject(content);

            action = response.getJSONObject("up").getJSONObject("acc").getJSONArray("main").get(0).toString();
            qiniuProperties.setAction(action);

            fetchUrl = response.getJSONObject("io").getJSONObject("src").getJSONArray("main").get(0).toString();
            qiniuProperties.setFetchUrl(fetchUrl);
        }
    }

}
