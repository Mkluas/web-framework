package cn.mklaus.framework.shiro;

import cn.mklaus.framework.ResponseProperties;
import cn.mklaus.framework.exception.BaseErrorEnum;
import cn.mklaus.framework.util.Https;
import cn.mklaus.framework.web.Response;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author klaus
 * @date 2019-09-12 10:16
 */
public class ShiroLoginFilter extends FormAuthenticationFilter {

    private ResponseProperties responseProperties;

    public ShiroLoginFilter(ResponseProperties responseProperties) {
        this.responseProperties = responseProperties;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //这里是个坑，如果不设置的接受的访问源，那么前端都会报跨域错误，因为这里还没到corsConfig里面
        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        Response resp = Response.error(BaseErrorEnum.ACCOUNT_UNAUTHENTICATED);
        Https.response(resp.build(responseProperties).toJSONString(), httpServletResponse);
        return false;
    }


}
