package cn.mklaus.framework.config;

import cn.mklaus.framework.config.secure.AdminSecureConfig;
import cn.mklaus.framework.service.AdminService;
import cn.mklaus.framework.service.impl.AdminServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author klausxie
 * @date 2020-08-08
 */
@Configuration
@EnableConfigurationProperties(AdminConfigurationProperties.class)
@Import(AdminSecureConfig.class)
public class AdminAutoConfiguration {

    private final AdminConfigurationProperties adminConfigurationProperties;

    public AdminAutoConfiguration(AdminConfigurationProperties adminConfigurationProperties) {
        this.adminConfigurationProperties = adminConfigurationProperties;
    }

    @Bean
    public AdminService adminService() {
        return new AdminServiceImpl(this.adminConfigurationProperties);
    }

    @Bean
    public AdminDataInitializer adminDataInitializer() {
        return new AdminDataInitializer(this.adminConfigurationProperties, adminService());
    }

}
