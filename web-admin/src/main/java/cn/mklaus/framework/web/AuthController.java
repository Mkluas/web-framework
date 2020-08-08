package cn.mklaus.framework.web;

import cn.mklaus.framework.config.secure.AdminHolder;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.service.AdminService;
import cn.mklaus.framework.bean.vo.AdminLoginVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author klaus
 * Created on 2019-09-11 19:16
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminService adminService;

    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation("登录")
    @PostMapping("login")
    public Response login(@Valid AdminLoginVO vo) {
        return Response.ok();
    }

    @ApiOperation("登出")
    @PostMapping("logout")
    public Response logout() {
        return Response.ok();
    }

    @ApiOperation("当前已登录管理员")
    @GetMapping("me")
    public Response me() {
        String loginAccount = AdminHolder.getLoginAccount();
        return Response.ok().put("admin", adminService.getByAccount(loginAccount));
    }

}
