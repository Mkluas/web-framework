package cn.mklaus.framework.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author klaus
 * @date 2019-09-12 00:15
 */
@ApiModel
@Data
@NoArgsConstructor
public class AdminCreateVO {

    @ApiModelProperty(required = true, value = "账号")
    @NotBlank(message = "帐号不能为空")
    @Pattern(regexp = "[0-9a-z]+", message = "仅支持字母、数字（建议为姓名拼音）")
    private String account;

    @ApiModelProperty(required = true, value = "密码")
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 32)
    private String password;

}
