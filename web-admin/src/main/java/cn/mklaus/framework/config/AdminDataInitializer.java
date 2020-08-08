package cn.mklaus.framework.config;


import cn.mklaus.framework.bean.dto.AdminDTO;
import cn.mklaus.framework.entity.Admin;
import cn.mklaus.framework.service.AdminService;
import cn.mklaus.framework.bean.vo.AdminCreateVO;
import cn.mklaus.framework.bean.vo.AdminUpdateVO;
import cn.mklaus.framework.bean.vo.AdminRolesVO;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.nutz.dao.util.Daos;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Mklaus
 * Created on 2018-02-27 下午4:14
 */
@Slf4j
@Component
public class AdminDataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final AdminConfigurationProperties adminConfigurationProperties;
    private final AdminService adminService;

    public AdminDataInitializer(AdminConfigurationProperties adminConfigurationProperties, AdminService adminService) {
        this.adminConfigurationProperties = adminConfigurationProperties;
        this.adminService = adminService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            initAdminData();
        }
    }

    private void initAdminData() {
        Daos.createTablesInPackage(adminService.dao(), Admin.class.getPackage().getName(), false);

        if (adminService.getByAccount(adminConfigurationProperties.getSuperAdmin()) == null) {
            log.info("Init admin data");

            AdminCreateVO cvo = new AdminCreateVO();
            cvo.setAccount("admin");
            cvo.setPassword(adminConfigurationProperties.getDefaultPassword());
            AdminDTO adminDTO = adminService.save(cvo).getData().getObject("admin", AdminDTO.class);

            AdminUpdateVO updateVO = new AdminUpdateVO();
            updateVO.setAdminId(adminDTO.getId());
            updateVO.setUsername("超级管理员");
            updateVO.setAvatar("http://img.chuangcifang.com/xie.jpg");
            adminService.update(updateVO, adminDTO.getId());

            AdminRolesVO rvo = new AdminRolesVO();
            JSONArray roles = new JSONArray();
            roles.add(adminConfigurationProperties.getSuperRole());
            rvo.setRoles(roles);
            rvo.setAdminId(adminDTO.getId());
            adminService.updateRoles(rvo);
        }
    }
}

