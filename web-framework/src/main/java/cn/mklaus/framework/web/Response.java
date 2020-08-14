package cn.mklaus.framework.web;

import cn.mklaus.framework.ResponseProperties;
import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.exception.ErrorInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

/**
 * 用于对api请求结果的封装
 *
 * @author Mklaus
 * Created on 2018-01-02 下午1:10
 */
@Data
public class Response {

    private static final int SUCCESS_CODE = 0;
    private static final int ERROR_CODE = 1;

    private int errCode;

    private String errMsg;

    private JSONObject data;

    private Response() {
        this.data = new JSONObject();
    }

    private static Response newInstance(){
        return new Response();
    }

    public static Response ok() {
        return newInstance().errCode(SUCCESS_CODE).errMsg("ok");
    }

    public static Response error(String errMsg) {
        return newInstance().errCode(ERROR_CODE).errMsg(errMsg);
    }

    public static Response error(ErrorInfo errorInfo) {
        return newInstance().errCode(errorInfo.getErrCode()).errMsg(errorInfo.getErrMsg());
    }

    public static Response with(ServiceResult result) {
        Response response = newInstance();
        response.errCode(result.getCode()).errMsg(result.getMsg());
        response.data.putAll(result.getData());
        return response;
    }

    // Builder

    public Response errCode(int errCode) {
        this.errCode = errCode;
        return this;
    }

    public Response errMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public Response put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Response putAll(Map map) {
        this.data.putAll(map);
        return this;
    }

    public Response putEntity(Object entity) {
        this.data.putAll(JSON.parseObject(JSON.toJSONString(entity)));
        return this;
    }

    public JSONObject build() {
        this.data.put("errCode", this.errCode);
        this.data.put("errMsg", this.errMsg);
        return this.data;
    }

    public JSONObject build(ResponseProperties properties) {
        if (properties.isUseDataKey()) {
            JSONObject resp = new JSONObject();
            resp.put(properties.getErrCodeKey(), this.errCode);
            resp.put(properties.getErrMsgKey(), this.errMsg);
            resp.put(properties.getDataKey(), this.data);
            return resp;
        } else {
            this.data.put(properties.getErrCodeKey(), this.errCode);
            this.data.put(properties.getErrMsgKey(), this.errMsg);
            return this.data;
        }
    }

    @Override
    public String toString() {
        return build().toString();
    }

}
