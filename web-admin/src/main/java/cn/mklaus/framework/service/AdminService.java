package cn.mklaus.framework.service;

import cn.mklaus.framework.bean.PageVO;
import cn.mklaus.framework.bean.Pagination;
import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.dto.AdminDTO;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.base.BaseService;
import cn.mklaus.framework.vo.AdminCreateVO;
import cn.mklaus.framework.vo.AdminInfoVO;
import cn.mklaus.framework.vo.AdminRolesVO;
import cn.mklaus.framework.vo.PasswdVO;

/**
 * @author klaus
 * Created on 2019-09-11 23:32
 */
public interface AdminService extends BaseService<Admin> {

    /**
     * get admin by account
     * @param account account
     * @return Admin
     */
    Admin getByAccount(String account);

    /**
     * 获取指定ID的管理员信息
     * @param adminId   管理员ID
     * @return AdminDTO
     */
    AdminDTO getAdmin(int adminId);

    /**
     * 添加管理员
     * @param vo   管理员创建VO
     * @return 业务结果
     */
    ServiceResult saveAdmin(AdminCreateVO vo);

    /**
     * 获取管理员列表
     * @param pageVO pageVO
     * @return 分页与列表模型
     */
    Pagination listAdmin(PageVO pageVO);

    /**
     * 更新管理员信息
     * @param vo   管理员信息VO
     * @param adminId adminId
     * @return 业务结果
     */
    ServiceResult updateAdmin(AdminInfoVO vo, int adminId);

    /**
     * 删除指定ID的管理员
     * @param adminId   管理员ID
     * @return  业务结果
     */
    ServiceResult removeAdmin(int adminId);

    /**
     * 修改管理员密码
     * @param vo  封装新密码和旧密码的请求VO
     * @return  业务结果
     */
    ServiceResult passwd(PasswdVO vo);

    /**
     * 重置管理员密码
     * @param adminId   管理员ID
     * @return  业务结果
     */
    ServiceResult resetPassword(int adminId);


    /**
     * 禁止登录
     * @param adminId   adminID
     * @param forbid    禁止
     * @return  业务结果
     */
    ServiceResult changeForbid(int adminId, boolean forbid);


    /**
     * 更新角色
     * @param vo vo
     * @return 业务结果
     */
    ServiceResult updateRoles(AdminRolesVO vo);


}