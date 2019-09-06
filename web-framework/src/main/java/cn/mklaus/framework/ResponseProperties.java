package cn.mklaus.framework;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author klaus
 * Created on 2019-08-07 15:33
 */
@Data
@ConfigurationProperties(prefix = "cn.mklaus.response")
public class ResponseProperties {

    private String errCodeKey = "errCode";

    private String errMsgKey = "errMsg";

    private String dataKey = "data";

    private boolean useDataKey = false;

}
