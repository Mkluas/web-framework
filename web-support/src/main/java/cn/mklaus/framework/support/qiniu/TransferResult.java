package cn.mklaus.framework.support.qiniu;

import lombok.Data;

/**
 * @author klausxie
 * @date 2020-08-13
 */
@Data
public class TransferResult {

    private boolean ok;
    private String targetUrl;
    private String errMsg;

    public static TransferResult ok(String targetUrl) {
        TransferResult result = new TransferResult();
        result.ok = true;
        result.targetUrl = targetUrl;
        return result;
    }

    public static TransferResult error(String errMsg) {
        TransferResult result = new TransferResult();
        result.ok = false;
        result.errMsg = errMsg;
        return result;
    }

}
