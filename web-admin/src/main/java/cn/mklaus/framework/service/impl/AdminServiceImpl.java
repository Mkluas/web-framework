package cn.mklaus.framework.service.impl;

import cn.mklaus.framework.bean.PageVO;
import cn.mklaus.framework.bean.Pagination;
import cn.mklaus.framework.dto.AdminDTO;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.exception.BaseErrorEnum;
import cn.mklaus.framework.service.AdminService;
import cn.mklaus.framework.base.BaseServiceImpl;
import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.shiro.ShiroConfigProperties;
import cn.mklaus.framework.util.Langs;
import cn.mklaus.framework.vo.AdminCreateVO;
import cn.mklaus.framework.vo.AdminInfoVO;
import cn.mklaus.framework.vo.AdminRolesVO;
import cn.mklaus.framework.vo.PasswdVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.FieldMatcher;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author klaus
 * Created on 2019-09-11 23:32
 */
@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {

    @Resource
    private ShiroConfigProperties shiroConfigProperties;

    @Override
    public Admin getByAccount(String account) {
        return fetch(Cnd.where("account", "=", account));
    }

    @Override
    public AdminDTO getAdmin(int adminId) {
        Admin fetch = fetch(adminId);
        return fetch == null ? null : new AdminDTO(fetch);
    }

    @Override
    public Pagination listAdmin(Boolean forbid, PageVO pageVO) {
        SimpleCriteria cri = Cnd.cri();

        if (Strings.isNotBlank(pageVO.getKey())) {
            cri.where().and("concat(username,account,mobile)", "like", wrapSearchKey(pageVO.getKey()));
        }
        if (forbid != null) {
            cri.where().and("forbid", "=", forbid);
        }

        FieldMatcher matcher = FieldMatcher.make("", "^password|salt$", true);
        Pagination pagination = listPage(cri, matcher, pageVO.toPager());
        pagination.setList(AdminDTO.toList(pagination.getList()));
        return pagination;
    }

    @Override
    public ServiceResult saveAdmin(AdminCreateVO vo) {
        if (getByAccount(vo.getAccount()) != null) {
            return ServiceResult.error(BaseErrorEnum.ACCOUNT_ALREADY_EXIST);
        }

        String salt = Langs.uuid();
        String passwordHash = this.hashPassword(vo.getAccount(), salt, vo.getPassword());

        Admin admin = new Admin();
        admin.setForbid(false);
        admin.setAccount(vo.getAccount());
        admin.setSalt(salt);
        admin.setPassword(passwordHash);
        admin.setRoles(Collections.emptyList());

        insert(admin);
        return ServiceResult.ok().put("admin", new AdminDTO(admin));
    }

    @Override
    public ServiceResult updateAdmin(AdminInfoVO vo, int adminId) {
        Admin admin = fetch(adminId);
        Assert.notNull(admin, "管理员不存在");

        admin.setUsername(vo.getUsername());
        admin.setMobile(vo.getMobile());
        admin.setEmail(vo.getEmail());
        admin.setAvatar(vo.getAvatar());

        update(admin);
        return ServiceResult.ok().put("admin", new AdminDTO(admin));
    }

    @Override
    public ServiceResult removeAdmin(int adminId) {
        Admin current = (Admin) SecurityUtils.getSubject().getPrincipal();
        Assert.state(adminId != current.getId(), "不能删除自己");
        Admin admin = fetch(adminId);
        Assert.notNull(admin, "管理员不存在");
        Assert.state(!"admin".equals(admin.getAccount()),"不能删除超级管理员");
        delete(admin);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult passwd(PasswdVO vo) {
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        admin = getByAccount(admin.getAccount());

        String oldPasswordHash = hashPassword( admin.getAccount(), admin.getSalt(), vo.getOldPassword());
        if (!oldPasswordHash.equals(admin.getPassword())) {
            return ServiceResult.error(BaseErrorEnum.WRONG_PASSWORD);
        }

        String newPasswordHash = hashPassword( admin.getAccount(), admin.getSalt(), vo.getNewPassword());
        update(Chain.make("password", newPasswordHash), Cnd.where("account", "=", admin.getAccount()));
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult resetPassword(int adminId) {
        Admin admin = fetch(adminId);
        Assert.notNull(admin, "管理员不存在");

        String account = admin.getAccount();
        String salt = admin.getSalt();
        String newPasswordHash = hashPassword(account, salt, shiroConfigProperties.getDefaultAdminPassword());
        update(Chain.make("password", newPasswordHash), Cnd.where("id", "=", adminId));

        return ServiceResult.ok();
    }

    @Override
    public ServiceResult changeForbid(int adminId, boolean forbid) {
        Admin admin = fetch(adminId);
        Assert.notNull(admin, "管理员不存在");
        if ("admin".equals(admin.getAccount())) {
            return ServiceResult.error("不能禁止超级管理员");
        }

        admin.setForbid(forbid);
        update(admin);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult updateRoles(AdminRolesVO vo) {
        Admin admin = fetch(vo.getAdminId());
        Assert.notNull(admin, "管理员不存在");

        admin.setRoles(vo.getRoles());
        update(admin);
        return ServiceResult.ok();
    }

    private String hashPassword(String account, String salt, String password) {
        SimpleHash hash = new SimpleHash(
                shiroConfigProperties.getHashAlgorithmName(),
                password,
                (account + salt),
                shiroConfigProperties.getHashIterations());
        return hash.toHex();
    }

}