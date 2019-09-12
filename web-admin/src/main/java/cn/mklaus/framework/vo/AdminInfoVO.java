package cn.mklaus.framework.vo;

import cn.mklaus.framework.bean.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author klaus
 * @date 2019-09-12 00:21
 */
@ApiModel
@Data
@NoArgsConstructor
public class AdminInfoVO {

    @ApiModelProperty(value = "adminId, 更新管理员时必填", example = "1")
    @NotNull(groups = {Update.class}, message = "adminId不能为空")
    private Integer adminId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户名")
    private String mobile;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatar;

}
