package cn.mklaus.framework.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author klausxie
 * @date 2020-05-06
 */
@ConditionalOnMissingBean(ExceptionLogger.class)
@Component
public class DefaultExceptionLogger implements ExceptionLogger {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public void log(Exception e) {
        Throwable throwable =  e;
        while (throwable != null) {
            Arrays.stream(throwable.getStackTrace())
                    .filter(st -> st.getClassName().contains("cn.mklaus"))
                    .forEach(st -> logger.error(st.toString()));
            throwable = throwable.getCause();
        }
    }

}
