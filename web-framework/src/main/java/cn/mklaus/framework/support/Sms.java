package cn.mklaus.framework.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.nutz.http.Http;
import org.nutz.lang.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.joining;

/**
 * @author Mklaus
 * Created on 2018-01-27 下午3:11
 */
@ConditionalOnProperty(prefix = "cn.mklaus.sendcloud", value = "sms-key")
@EnableConfigurationProperties(Sms.Config.class)
@Component
public class Sms implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(Sms.class);
    private static final String STATUS_CODE = "statusCode";
    private static final int SUCCESS_CODE   = 200;
    private static Config config;

    private static Result send(String mobile, String templateId, String vars) {
        Map<String,String> params = new TreeMap<>(String::compareToIgnoreCase);
        params.put("smsUser", config.smsUser);
        params.put("templateId", templateId);
        params.put("phone", mobile);
        params.put("vars", vars);

        String paramStr = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(joining("&",config.smsKey + "&","&" + config.smsKey));

        String sign = Lang.md5(paramStr);
        params.put("signature", sign);

        Map<String, Object> parameters = new HashMap<>();
        parameters.putAll(params);

        JSONObject data = JSON.parseObject(Http.post(config.url, parameters, 3000));
        if (data.getIntValue(STATUS_CODE) == SUCCESS_CODE) {
            return new Result(true, "ok");
        } else {
            if (data.containsKey("message")) {
                logger.error("Send sms to {}. errMsg: {}", mobile, data.get("message"));
                return new Result(false, data.getString("message"));
            }
            return new Result(false, data.toJSONString());
        }
    }

    public static Result sendSms(String mobile, String code){
        String vars = "{\"Code\":\"" + code + "\"}";
        return send(mobile, config.templateId, vars);
    }

    public static class Result {
        private Boolean sendSuccess;
        private String errMsg;

        public Result(Boolean sendSuccess, String errMsg) {
            this.sendSuccess = sendSuccess;
            this.errMsg = errMsg;
        }

        public Boolean isSendSuccess() {
            return sendSuccess;
        }

        public String getErrMsg() {
            return errMsg;
        }
    }


    @Resource
    private Config configInstance;

    @Override
    public void afterPropertiesSet() throws Exception {
        config = this.configInstance;
    }

    @Data
    @ConfigurationProperties(prefix = "cn.mklaus.sendcloud")
    static class Config {
        private String url = "http://sendcloud.sohu.com/smsapi/send";
        private String smsKey;
        private String smsUser;
        private String templateId;
    }

}
