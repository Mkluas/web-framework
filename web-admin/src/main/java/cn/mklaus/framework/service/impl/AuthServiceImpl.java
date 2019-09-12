package cn.mklaus.framework.service.impl;

import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.service.AuthService;
import cn.mklaus.framework.vo.AdminLoginVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Service;

/**
 * @author klaus
 * @date 2019-09-11 19:06
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public ServiceResult login(AdminLoginVO vo) {
        UsernamePasswordToken token = new UsernamePasswordToken(vo.getAccount(), vo.getPassword(), vo.getRememberMe());
        SecurityUtils.getSubject().login(token);
        return ServiceResult.ok();
    }

}
