package cn.mklaus.framework.web;

import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.dto.AdminDTO;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.exception.BaseErrorEnum;
import cn.mklaus.framework.service.AuthService;
import cn.mklaus.framework.vo.AdminLoginVO;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static org.apache.shiro.web.util.WebUtils.SAVED_REQUEST_KEY;

/**
 * @author klaus
 * @date 2019-09-11 19:16
 */
@Api(value = "AuthController", tags = {"后台认证登录接口" })
@RestController
@RequestMapping("${cn.mklaus.shiro.auth-prefix:/api/auth}")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ApiOperation("登录")
    @PostMapping("login")
    public JSONObject login(@Valid AdminLoginVO vo) {
        ServiceResult result = authService.login(vo);
        return Response.with(result).build();
    }

    @ApiOperation("登出")
    @PostMapping("logout")
    public JSONObject logout() {
        SecurityUtils.getSubject().logout();
        return Response.ok().build();
    }

    @ApiOperation("当前已登录管理员")
    @GetMapping("me")
    public JSONObject me() {
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        return Response.ok().put("admin", new AdminDTO(admin)).build();
    }

    @ApiIgnore
    @GetMapping("error")
    public Response notLogin() {
        return Response.error(BaseErrorEnum.ACCOUNT_UNAUTHENTICATED)
                .put("requestURI", getRequestUri());
    }

    @ApiIgnore
    @GetMapping("403")
    public Response noPermission() {
        return Response.error(BaseErrorEnum.PERMISSION_DENIED)
                .put("url", getRequestUri());
    }

    private static String getRequestUri() {
        String requestUri = "unknown";
        Session session =  SecurityUtils.getSubject().getSession(false);
        if (session != null) {
            SavedRequest savedRequest = (SavedRequest) session.getAttribute(SAVED_REQUEST_KEY);
            if (savedRequest != null) {
                requestUri = savedRequest.getRequestURI();
            }
        }
        return requestUri;
    }
}
