package cn.mklaus.framework.service;

import cn.mklaus.framework.bean.ServiceResult;
import cn.mklaus.framework.vo.AdminLoginVO;

/**
 * @author klaus
 * @date 2019-09-11 19:05
 */
public interface AuthService {

    ServiceResult login(AdminLoginVO vo);

}
