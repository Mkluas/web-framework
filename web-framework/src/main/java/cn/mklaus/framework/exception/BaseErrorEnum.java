package cn.mklaus.framework.exception;

/**
 * @author Mklaus
 * Created on 2018-02-05 上午11:56
 */
public enum BaseErrorEnum implements ErrorInfo {

    /**
     * 管理后台
     */
    ACCOUNT_UNAUTHENTICATED(40002, "帐号未认证或登录过期，请重新登录"),
    PERMISSION_DENIED(40003, "没有权限操作"),
    ACCOUNT_LOCK(40005, "帐号锁定"),

    /**
     * 前端接口
     */
    ACCOUNT_ALREADY_EXIST(40020, "账号已存在"),
    WRONG_ACCOUNT_OR_PASSWORD(40021, "账号或密码错误"),
    WRONG_PASSWORD(40022, "密码错误"),
    TOKEN_INVALID(40025, "登录过期"),
    TOKEN_MISSING(40026, "缺少登录凭证"),

    ;

    private final int errCode;

    private final String errMsg;

    BaseErrorEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

}
