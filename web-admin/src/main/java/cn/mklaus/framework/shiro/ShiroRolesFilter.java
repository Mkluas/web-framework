package cn.mklaus.framework.shiro;

import cn.mklaus.framework.ResponseProperties;
import cn.mklaus.framework.exception.BaseErrorEnum;
import cn.mklaus.framework.util.Https;
import cn.mklaus.framework.web.Response;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author klaus
 * @date 2019-09-12 12:11
 */
public class ShiroRolesFilter extends RolesAuthorizationFilter {

    private ResponseProperties responseProperties;

    public ShiroRolesFilter(ResponseProperties responseProperties) {
        this.responseProperties = responseProperties;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Response resp = Response.error(BaseErrorEnum.PERMISSION_DENIED);
        Https.response(resp.build(responseProperties).toJSONString(), httpServletResponse);
        return false;
    }

}
