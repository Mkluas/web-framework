package cn.mklaus.framework.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author klaus
 * @date 2019-09-11 23:56
 */
@ApiModel
@Data
@ToString
public class AdminLoginVO {

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "帐号不能为空")
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    private Boolean rememberMe;

    public AdminLoginVO() {
        this.rememberMe = false;
    }

}
