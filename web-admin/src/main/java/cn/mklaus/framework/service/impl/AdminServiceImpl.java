package cn.mklaus.framework.service.impl;

import cn.mklaus.framework.bean.PageVO;
import cn.mklaus.framework.bean.Pagination;
import cn.mklaus.framework.bean.dto.AdminDTO;
import cn.mklaus.framework.config.AdminConfigurationProperties;
import cn.mklaus.framework.config.secure.PasswordEncipher;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.exception.BaseErrorEnum;
import cn.mklaus.framework.service.AdminService;
import cn.mklaus.framework.base.BaseServiceImpl;
import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.util.Langs;
import cn.mklaus.framework.bean.vo.AdminCreateVO;
import cn.mklaus.framework.bean.vo.AdminUpdateVO;
import cn.mklaus.framework.bean.vo.AdminRolesVO;
import cn.mklaus.framework.bean.vo.AdminPasswdVO;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.FieldMatcher;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.lang.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * @author klaus
 * Created on 2019-09-11 23:32
 */
@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {


    private final AdminConfigurationProperties adminConfigurationProperties;

    public AdminServiceImpl(AdminConfigurationProperties adminConfigurationProperties) {
        this.adminConfigurationProperties = adminConfigurationProperties;
    }

    @Override
    public Admin getByAccount(String account) {
        return fetch(Cnd.where("account", "=", account));
    }


    @Override
    public Pagination page(Boolean forbid, PageVO pageVO) {
        SimpleCriteria cri = Cnd.cri();

        if (Strings.isNotBlank(pageVO.getKey())) {
            cri.where().and("concat(username,account,mobile)", "like", wrapSearchKey(pageVO.getKey()));
        }
        if (forbid != null) {
            cri.where().and("forbid", "=", forbid);
        }

        FieldMatcher matcher = FieldMatcher.make("", "^password|salt$", true);
        Pagination pagination = listPage(cri, matcher, pageVO.toPager());
        pagination.setList(AdminDTO.createList((List<Admin>) pagination.getList()));
        return pagination;
    }

    @Override
    public ServiceResult save(AdminCreateVO vo) {
        Assert.isNull(getByAccount(vo.getAccount()), BaseErrorEnum.ACCOUNT_ALREADY_EXIST.getErrMsg());

        String salt = Langs.uuid();
        String passwordHash = PasswordEncipher.encrypt(vo.getPassword(), salt);

        Admin admin = new Admin();
        admin.setForbid(false);
        admin.setAccount(vo.getAccount());
        admin.setSalt(salt);
        admin.setPassword(passwordHash);
        admin.setRoles(Collections.emptyList());

        insert(admin);
        return ServiceResult.ok().put("admin", AdminDTO.create(admin));
    }

    @Override
    public ServiceResult update(AdminUpdateVO vo, int adminId) {
        Admin admin = checkGet(adminId);
        BeanUtils.copyProperties(vo, admin);
        update(admin);
        return ServiceResult.ok().put("admin", AdminDTO.create(admin));
    }

    @Override
    public ServiceResult remove(int adminId) {
        Admin admin = checkGet(adminId);
        boolean isSuperAdmin = adminConfigurationProperties.getSuperAdmin().equals(admin.getAccount());
        Assert.state(!isSuperAdmin,"不能删除超级管理员");
        delete(admin);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult passwd(AdminPasswdVO vo, String loginAccount) {
        Admin admin = getByAccount(loginAccount);

        String oldEncryptPassword= PasswordEncipher.encrypt(vo.getOldPassword(), admin.getSalt());
        if (!oldEncryptPassword.equals(admin.getPassword())) {
            return ServiceResult.error(BaseErrorEnum.WRONG_PASSWORD);
        }

        String newEncryptPassword = PasswordEncipher.encrypt( vo.getNewPassword(), admin.getSalt());
        update(Chain.make("password", newEncryptPassword), Cnd.where("account", "=", admin.getAccount()));
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult resetPassword(int adminId) {
        Admin admin = checkGet(adminId);

        String salt = admin.getSalt();
        String encrypt = PasswordEncipher.encrypt(adminConfigurationProperties.getDefaultPassword(), salt);
        update(Chain.make("password", encrypt), Cnd.where("id", "=", adminId));

        return ServiceResult.ok();
    }

    @Override
    public ServiceResult changeForbid(int adminId, boolean forbid) {
        Admin admin = checkGet(adminId);
        boolean isSuperAdmin = adminConfigurationProperties.getSuperAdmin().equals(admin.getAccount());
        if (isSuperAdmin) {
            return ServiceResult.error("不能禁止超级管理员");
        }
        admin.setForbid(forbid);
        update(admin);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult updateRoles(AdminRolesVO vo) {
        Admin admin = checkGet(vo.getAdminId());
        admin.setRoles(vo.getRoles());
        update(admin);
        return ServiceResult.ok();
    }

}