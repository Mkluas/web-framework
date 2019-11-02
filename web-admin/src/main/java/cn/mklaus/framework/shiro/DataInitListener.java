package cn.mklaus.framework.shiro;


import cn.mklaus.framework.dto.AdminDTO;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.service.AdminService;
import cn.mklaus.framework.vo.AdminCreateVO;
import cn.mklaus.framework.vo.AdminInfoVO;
import cn.mklaus.framework.vo.AdminRolesVO;
import com.alibaba.fastjson.JSONArray;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.Daos;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * @author Mklaus
 * Created on 2018-02-27 下午4:14
 */
@Service
@ConditionalOnProperty(prefix = "cn.mklaus.shiro.init", value = "true", matchIfMissing = true)
public class DataInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ShiroConfigProperties shiroConfigProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            initAdminData();
        }
    }

    private void initAdminData() {
        Daos.createTablesInPackage(adminService.dao(), Admin.class.getPackage().getName(), false);

        if (adminService.count(Cnd.format("account = 'admin'")) == 0) {
            Logs.get().info("Init admin data");

            AdminCreateVO cvo = new AdminCreateVO();
            cvo.setAccount("admin");
            cvo.setPassword(shiroConfigProperties.getDefaultAdminPassword());
            AdminDTO adminDTO = adminService.saveAdmin(cvo).getData().getObject("admin", AdminDTO.class);

            AdminInfoVO infoVO = new AdminInfoVO();
            infoVO.setAdminId(adminDTO.getId());
            infoVO.setUsername("超级管理员");
            infoVO.setAvatar("http://img.chuangcifang.com/xie.jpg");
            adminService.updateAdmin(infoVO, adminDTO.getId());

            AdminRolesVO rvo = new AdminRolesVO();
            JSONArray roles = new JSONArray();
            roles.add(shiroConfigProperties.getSuperRole());
            rvo.setRoles(roles);
            rvo.setAdminId(adminDTO.getId());
            adminService.updateRoles(rvo);
        }
    }
}

