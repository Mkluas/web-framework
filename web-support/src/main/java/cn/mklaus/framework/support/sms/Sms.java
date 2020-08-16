package cn.mklaus.framework.support.sms;

import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.support.web.SmsController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.nutz.http.Http;
import org.nutz.lang.Lang;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.joining;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Slf4j
@ConditionalOnProperty(value = "cn.mklaus.sendcloud.sms-key")
@Component
@EnableConfigurationProperties(SendCloudProperties.class)
@Import(SmsController.class)
public class Sms {

    private static final String STATUS_CODE = "statusCode";
    private static final int SUCCESS_CODE   = 200;

    private final SendCloudProperties sendCloudProperties;

    public Sms(SendCloudProperties sendCloudProperties) {
        this.sendCloudProperties = sendCloudProperties;
    }

    private ServiceResult send(String mobile, String templateId, String vars) {
        Map<String,String> params = new TreeMap<>(String::compareToIgnoreCase);
        params.put("smsUser", sendCloudProperties.getSmsUser());
        params.put("templateId", templateId);
        params.put("phone", mobile);
        params.put("vars", vars);

        String paramStr = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(joining("&",sendCloudProperties.getSmsKey() + "&","&" + sendCloudProperties.getSmsKey()));
        params.put("signature", Lang.md5(paramStr));

        JSONObject data = JSON.parseObject(Http.post(sendCloudProperties.getUrl(), new HashMap<>(params), 3000));
        if (data.getIntValue(STATUS_CODE) == SUCCESS_CODE) {
            return ServiceResult.ok();
        } else {
            String errMsg = data.containsKey("message") ? data.getString("message") : data.toJSONString();
            return ServiceResult.error(errMsg);
        }
    }

    public ServiceResult sendSms(String mobile, String code){
        String vars = "{\"Code\":\"" + code + "\"}";
        return send(mobile, sendCloudProperties.getTemplateId(), vars);
    }

}
