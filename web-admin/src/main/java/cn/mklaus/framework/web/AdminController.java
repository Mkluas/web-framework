package cn.mklaus.framework.web;

import cn.mklaus.framework.bean.PageVO;
import cn.mklaus.framework.bean.Pagination;
import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.bean.dto.AdminDTO;
import cn.mklaus.framework.bean.vo.AdminCreateVO;
import cn.mklaus.framework.bean.vo.AdminPasswdVO;
import cn.mklaus.framework.bean.vo.AdminRolesVO;
import cn.mklaus.framework.bean.vo.AdminUpdateVO;
import cn.mklaus.framework.config.secure.AdminHolder;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.service.AdminService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author klaus
 * Created on 2019-09-12 00:57
 */
@RestController
@RequestMapping("/api/backend/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @ApiOperation("根据adminId获取管理员")
    @GetMapping("get")
    @ResponseBody
    public Response getAdmin(@ApiParam(example = "1") @RequestParam Integer adminId) {
        Admin admin = adminService.get(adminId);
        return Response.ok().put("admin", AdminDTO.create(admin));
    }

    @ApiOperation("管理员列表")
    @GetMapping("list")
    @ResponseBody
    public Response listAdmin(@Valid PageVO pageVO, Boolean forbid) {
        Pagination pagination = adminService.page(forbid, pageVO);
        return Response.ok().put("pager", pagination);
    }

    @ApiOperation("添加管理员")
    @PostMapping("save")
    @ResponseBody
    public Response saveAdmin(@Validated AdminCreateVO vo) {
        ServiceResult result = adminService.save(vo);
        return Response.with(result);
    }

    @ApiOperation("更新管理员")
    @PostMapping("update")
    @ResponseBody
    public Response updateAdmin(@Validated AdminUpdateVO vo) {
        ServiceResult result = adminService.update(vo, vo.getAdminId());
        return Response.with(result);
    }

    @ApiOperation("删除管理员")
    @PostMapping("remove")
    @ResponseBody
    public Response removeAdmin(@ApiParam(example = "1") @RequestParam Integer adminId) {
        ServiceResult result = adminService.remove(adminId);
        return Response.with(result);
    }

    @ApiOperation("重置密码")
    @PostMapping(value = "resetpassword")
    @ResponseBody
    public Response resetPassword(@ApiParam(example = "1") @RequestParam Integer adminId) {
        ServiceResult result = adminService.resetPassword(adminId);
        return Response.with(result);
    }

    @ApiOperation("禁止登录")
    @PostMapping(value = "forbid")
    @ResponseBody
    public Response forbid(@ApiParam(example = "1") @RequestParam Integer adminId,
                           @ApiParam(required = true, value = "true or false") @RequestParam Boolean forbid) {
        ServiceResult result = adminService.changeForbid(adminId, forbid);
        return Response.with(result);
    }

    @ApiOperation("授予角色")
    @PostMapping(value = "roles")
    @ResponseBody
    public Response roles(@Valid AdminRolesVO vo) {
        ServiceResult result = adminService.updateRoles(vo);
        return Response.with(result);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "passwd")
    @ResponseBody
    public JSONObject passwd(@Valid AdminPasswdVO adminPasswdVO) {
        String loginAccount = AdminHolder.getLoginAccount();
        ServiceResult result = adminService.passwd(adminPasswdVO, loginAccount);
        return Response.with(result).build();
    }

}
