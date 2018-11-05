package cn.mklaus.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Mklaus
 * Created on 2018-03-29 上午10:05
 */
@Data
@ConfigurationProperties(
        prefix = "cn.mklaus.config"
)
public class AutoConfigurationProperties {

    private String basepackage;

    private boolean performance;

    private boolean eventPublisher = true;

    private boolean logRequest = true;

    private String errorTemplatePath = "";

    private String error404TemplatePath = "";

    private boolean includeStackTrace = true;

    private boolean showErrorDetail = true;

    private boolean useDefaultResourceHandler = true;

}
