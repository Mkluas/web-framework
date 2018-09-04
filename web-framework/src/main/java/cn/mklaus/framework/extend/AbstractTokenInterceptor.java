package cn.mklaus.framework.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @author Mklaus
 * @date 2018-01-02 下午1:41
 */
public abstract class AbstractTokenInterceptor<T> extends HandlerInterceptorAdapter {

    private static Logger log = LoggerFactory.getLogger(AbstractTokenInterceptor.class);

    protected List<String> getUriList() {
        return Collections.emptyList();
    }

    protected List<String> getPrefixUriList() {
        return Collections.emptyList();
    }

    protected List<String> getIgnoreUriList() {
        return Collections.emptyList();
    }

    protected List<String> getIgnorePrefixUriList() {
        return Collections.emptyList();
    }

    protected boolean passMe(HttpServletRequest req) {
        return false;
    }

    protected String getName() {
        return "AbstractTokenInterceptor";
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        String uri = req.getRequestURI();

        if (matchByUriList(getIgnoreUriList(), uri) || matchByPrefixUriList(getIgnorePrefixUriList(), uri)) {
            log.debug("{} ignore: {}", getName(), uri);
            return true;
        }

        if (passMe(req)) {
            log.debug("{} pass me: {}", getName(), uri);
            return true;
        }

        if (matchByUriList(getUriList(), uri) || matchByPrefixUriList(getPrefixUriList(), uri)) {
            log.info("{} intercept: {} {}", getName(), uri, req.getMethod());
            T token = this.extraToken(req);
            return this.handleToken(token, req, resp);
        }

        log.debug("{} not interceptor, default pass : {}", getName(), uri);
        return true;
    }

    private boolean matchByUriList(List<String> urlList, String uri) {
        return urlList.stream().anyMatch(uri::equalsIgnoreCase);
    }

    private boolean matchByPrefixUriList(List<String> urlList, String uri) {
        return urlList.stream().anyMatch(uri::startsWith);
    }

    protected abstract T extraToken(HttpServletRequest req);

    protected abstract boolean handleToken(T token, HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
