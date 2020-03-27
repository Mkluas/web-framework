package cn.mklaus.framework.web;

import cn.mklaus.framework.bean.*;
import cn.mklaus.framework.dto.AdminDTO;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.service.AdminService;
import cn.mklaus.framework.vo.AdminCreateVO;
import cn.mklaus.framework.vo.AdminInfoVO;
import cn.mklaus.framework.vo.AdminRolesVO;
import cn.mklaus.framework.vo.PasswdVO;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author klaus
 * Created on 2019-09-12 00:57
 */
@Api(value = "AdminController", tags = {"后台管理员接口" })
@RestController
@RequestMapping("${cn.mklaus.shiro.admin-prefix:/api/backend/admin}")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @ApiOperation("根据adminId获取管理员")
    @GetMapping("get")
    @ResponseBody
    public Response getAdmin(@ApiParam(example = "1") @RequestParam Integer adminId) {
        AdminDTO adminDTO = adminService.getAdmin(adminId);
        return Response.ok().put("admin", adminDTO);
    }

    @ApiOperation("管理员列表")
    @GetMapping("list")
    @ResponseBody
    public Response listAdmin(@Valid PageVO pageVO, Boolean forbid) {
        Pagination pagination = adminService.listAdmin(forbid, pageVO);
        return Response.ok().put("pager", pagination);
    }

    @ApiOperation("添加管理员")
    @PostMapping("save")
    @ResponseBody
    public Response saveAdmin(@Validated AdminCreateVO vo) {
        ServiceResult result = adminService.saveAdmin(vo);
        return Response.with(result);
    }

    @ApiOperation("更新管理员")
    @PostMapping("update")
    @ResponseBody
    public Response updateAdmin(@Validated(Update.class) AdminInfoVO vo) {
        ServiceResult result = adminService.updateAdmin(vo, vo.getAdminId());
        return Response.with(result);
    }

    @ApiOperation("删除管理员")
    @PostMapping("remove")
    @ResponseBody
    public Response removeAdmin(@ApiParam(example = "1") @RequestParam Integer adminId) {
        ServiceResult result = adminService.removeAdmin(adminId);
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
    public JSONObject passwd(@Valid PasswdVO passwdVO) {
        ServiceResult result = adminService.passwd(passwdVO);
        return Response.with(result).build();
    }

    @ApiOperation("当前登录管理员更新自身信息")
    @PostMapping("info")
    @ResponseBody
    public Response info(@Validated AdminInfoVO vo) {
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        ServiceResult result = adminService.updateAdmin(vo, admin.getId());
        return Response.with(result);
    }

}
