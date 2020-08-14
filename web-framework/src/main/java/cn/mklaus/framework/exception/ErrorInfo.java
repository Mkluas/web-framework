package cn.mklaus.framework.exception;

/**
 * @author Mklaus
 * Created on 2018-02-05 上午11:55
 */
public interface ErrorInfo {

    /**
     * errCode = 0, login ok
     * errCode != 0, logic error
     * @return int
     */
    int getErrCode();

    /**
     * show error message when logic error happen
     * @return error message
     */
    String getErrMsg();

}
