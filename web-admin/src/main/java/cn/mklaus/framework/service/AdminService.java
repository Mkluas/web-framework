package cn.mklaus.framework.service;

import cn.mklaus.framework.bean.PageVO;
import cn.mklaus.framework.bean.Pagination;
import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.base.BaseService;
import cn.mklaus.framework.bean.vo.AdminCreateVO;
import cn.mklaus.framework.bean.vo.AdminUpdateVO;
import cn.mklaus.framework.bean.vo.AdminRolesVO;
import cn.mklaus.framework.bean.vo.AdminPasswdVO;

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
     * 添加管理员
     * @param vo   管理员创建VO
     * @return 业务结果
     */
    ServiceResult save(AdminCreateVO vo);

    /**
     * 获取管理员列表
     * @param pageVO pageVO
     * @return 分页与列表模型
     */
    Pagination page(Boolean forbid, PageVO pageVO);

    /**
     * 更新管理员信息
     * @param vo   管理员信息VO
     * @param adminId adminId
     * @return 业务结果
     */
    ServiceResult update(AdminUpdateVO vo, int adminId);

    /**
     * 删除指定ID的管理员
     * @param adminId   管理员ID
     * @return  业务结果
     */
    @Override
    ServiceResult remove(int adminId);

    /**
     * 修改管理员密码
     * @param vo  封装新密码和旧密码的请求VO
     * @return  业务结果
     */
    ServiceResult passwd(AdminPasswdVO vo, String loginAccount);

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