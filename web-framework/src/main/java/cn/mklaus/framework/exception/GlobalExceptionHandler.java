package cn.mklaus.framework.exception;

import cn.mklaus.framework.web.Response;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author Mklaus
 * Created on 2018-01-03 下午6:11
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Response missingServletRequestParameter(MissingServletRequestParameterException e, HttpServletRequest req) {
        return internalHandler("缺少参数：" + e.getParameterName(), e);
    }

    @ExceptionHandler(BindException.class)
    public Response bindException(BindException e, HttpServletRequest req) throws Exception {
        checkIfHtml(new BindException(e.getBindingResult()), req);
        List<ObjectError> errors = e.getAllErrors();
        String errMsg = errors.isEmpty() ? "未知绑定错误" : errors.get(0).getDefaultMessage();
        return internalHandler(errMsg, e);
    }

    @ExceptionHandler(SQLException.class)
    public Response missingServletRequestParameter(SQLException e) {
        String errMsg = 1366 == e.getErrorCode() ? "Emoji保存失败" : e.getMessage();
        return internalHandler(errMsg, e);
    }

    private Response internalHandler(String errMsg, Exception e) {
        logException(e);
        return Response.error(errMsg);
    }

    private void logException(Exception e) {
        logger.error("GlobalExceptionHandler: " + e.getMessage());
    }

    private static final String X_REQUESTED_WITH = "x-requested-with";
    private static final String ACCEPT = "accept";
    private void checkIfHtml(Exception e, HttpServletRequest req) throws Exception {
        String xRequestedWith = req.getHeader(X_REQUESTED_WITH);
        String accept = req.getHeader(ACCEPT);
        if (Strings.isBlank(xRequestedWith)
                && !Objects.isNull(accept)
                && accept.contains("html")) {
            logger.info("Filter to ErrorController");
            throw e;
        }
    }

}
