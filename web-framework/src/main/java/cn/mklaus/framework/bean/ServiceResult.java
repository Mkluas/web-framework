package cn.mklaus.framework.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

/**
 * 用于对业务逻辑处理结果的封装
 *
 * @author Mklaus
 * @date 2018-01-02 下午12:10
 */
@Data
public class ServiceResult {

    private static final String ENTITY_KEY = "SERVICE_RESULT_ENTITY_KEY";
    private static final int SUCCESS_CODE = 0;
    private static final int ERROR_CODE = 1;

    private int code;

    private String msg;

    private JSONObject data;

    private ServiceResult() {
        this.data = new JSONObject();
    }

    private ServiceResult(int code, String msg) {
        this();
        this.code = code;
        this.msg = msg;
    }

    public static ServiceResult ok() {
        return new ServiceResult(SUCCESS_CODE, "ok");
    }

    public static ServiceResult error(String msg) {
        return new ServiceResult(ERROR_CODE, msg);
    }

    public static ServiceResult error(ErrorInfo errorInfo) {
        return error(errorInfo.getErrCode(), errorInfo.getErrMsg());
    }

    public static ServiceResult error(int code, String msg) {
        return new ServiceResult(code, msg);
    }

    public boolean isOk() {
        return this.code == 0;
    }

    public ServiceResult put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public ServiceResult putAll(Map map) {
        this.data.putAll(map);
        return this;
    }

    public ServiceResult putEntity(Object entity) {
        this.put(ENTITY_KEY, entity);
        return this;
    }

    public <T> T getEntity(Class<T> classOfT) {
        return classOfT.cast(this.data.get(ENTITY_KEY));
    }

}
