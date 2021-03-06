package cn.mklaus.framework.exception;

import cn.mklaus.framework.ResponseProperties;
import cn.mklaus.framework.web.Response;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author Mklaus
 * Created on 2018-01-03 下午6:11
 */
@Order
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ExceptionLogger exceptionLogger;

    private final ResponseProperties responseProperties;

    public GlobalExceptionHandler(ResponseProperties responseProperties, ExceptionLogger exceptionLogger) {
        this.responseProperties = responseProperties;
        this.exceptionLogger = exceptionLogger;
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Response nullPointerException(NullPointerException e, HttpServletRequest req, HttpServletResponse resp) {
        String errMsg = e.getMessage();
        if (Objects.isNull(errMsg) || errMsg.length() == 0) {
            if (e.getStackTrace().length > 0) {
                errMsg = e.getStackTrace()[0].toString();
            }
        }

        return internalHandler("空指针异常：" + errMsg, e, req, resp);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Response missingServletRequestParameter(MissingServletRequestParameterException e, HttpServletRequest req, HttpServletResponse resp) {
        return internalHandler("缺少参数：" + e.getParameterName(), e, req, resp);
    }

    @ExceptionHandler(BindException.class)
    public Response bindException(BindException e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<ObjectError> errors = e.getAllErrors();
        String errMsg = errors.isEmpty() ? "未知绑定错误" : errors.get(0).getDefaultMessage();
        return internalHandler(errMsg, e, req, resp);
    }

    @ExceptionHandler(SQLException.class)
    public Response handleSQLException(SQLException e, HttpServletRequest req, HttpServletResponse resp) {
        String errMsg = 1366 == e.getErrorCode() ? "Emoji保存失败" : e.getMessage();
        return internalHandler(errMsg, e, req, resp);
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e, HttpServletRequest req, HttpServletResponse resp) {
        return internalHandler(e.getMessage(), e, req, resp);
    }

    private Response internalHandler(String errMsg, Exception e, HttpServletRequest req, HttpServletResponse resp) {
        logException(e);
        if (responseProperties.isUseHttpStatus()) {
            resp.setStatus(500);
        }
        return Response.error(errMsg);
    }

    private void logException(Exception e) {
        logger.error("GlobalExceptionHandler: " + e.getMessage());
        exceptionLogger.log(e);
    }

}
