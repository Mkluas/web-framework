package cn.mklaus.framework.service;

import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.vo.AdminLoginVO;

/**
 * @author klaus
 * Created on 2019-09-11 19:05
 */
public interface AuthService {

    /**
     * 登录
     * @param vo AdminLoginVO
     * @return 业务结果
     */
    ServiceResult login(AdminLoginVO vo);

}
