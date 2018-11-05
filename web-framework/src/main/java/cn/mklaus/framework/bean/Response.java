package cn.mklaus.framework.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
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

    public static Response error(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();
        String errMsg = errors.isEmpty() ? "未知错误" : errors.get(0).getDefaultMessage();

        return newInstance()
                .errCode(BaseErrorEnum.PARAMETER_ERROR.getErrCode())
                .errMsg(errMsg);
    }

    public static Response errorAll(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();

        StringBuilder sb = new StringBuilder("绑定参数错误：【 ");
        for (int i = 0; i < errors.size(); i++) {
            ObjectError error = errors.get(i);
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                sb.append(fieldError.getField()).append(": ");
            }

            sb.append(error.getDefaultMessage()).append("; ");
        }
        String errMsg = sb.append(" 】").toString();

        return newInstance()
                .errCode(BaseErrorEnum.PARAMETER_ERROR.getErrCode())
                .errMsg(errMsg);
    }

    public static Response withResult(ServiceResult result) {
        Response response = newInstance();
        response.errCode(result.getCode()).errMsg(result.getMsg());
        response.data.putAll(result.getData());
        return response;
    }

    public Response put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Response putAll(Map map) {
        this.data.putAll(map);
        return this;
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

    public JSONObject data(Object data) {
        this.data.put("errCode", this.errCode);
        this.data.put("errMsg", this.errMsg);
        this.data.put("data", data);
        return this.data;
    }

    public JSONObject build() {
        this.data.put("errCode", this.errCode);
        this.data.put("errMsg", this.errMsg);
        return this.data;
    }
}
