package cn.mklaus.framework.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author klausxie
 * @date 2020-08-08
 */
@Data
@ToString
@ConfigurationProperties(prefix = "cn.mklaus.admin")
public class AdminConfigurationProperties {

    private String superAdmin;

    private String superRole;

    private String defaultPassword;

    public AdminConfigurationProperties() {
        this.superAdmin = "admin";
        this.superRole = "root";
        this.defaultPassword = "12345678";
    }
}
