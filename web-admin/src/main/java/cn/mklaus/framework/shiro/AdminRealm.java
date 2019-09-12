package cn.mklaus.framework.shiro;

import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.service.AdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * @author klaus
 * @date 2019-09-11 23:22
 */
public class AdminRealm extends AuthorizingRealm {

    @Autowired
    private AdminService adminService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Admin admin = (Admin) super.getAvailablePrincipal(principals);
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
        simpleAuthorInfo.addRoles(admin.getRoles());
        simpleAuthorInfo.addStringPermission(admin.getAccount());
        return simpleAuthorInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String account = token.getPrincipal().toString();
        Assert.state(Strings.isNotBlank(account), "账号不能为空");
        Admin admin = adminService.getByAccount(account);
        if (admin == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        if (admin.getForbid()) {
            throw new DisabledAccountException("禁止登录，请联系管理员");
        }
        return new SimpleAuthenticationInfo(admin, admin.getPassword(), ByteSource.Util.bytes(account + admin.getSalt()), getName());
    }

    @Override
    public String getName() {
        return "AdminRealm";
    }

}
