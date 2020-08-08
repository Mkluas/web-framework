package cn.mklaus.framework.bean.vo;

import cn.mklaus.framework.bean.Update;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author klaus
 * Created on 2019-09-12 00:21
 */
@ApiModel
@Data
@NoArgsConstructor
public class AdminUpdateVO {

    @NotNull(groups = {Update.class}, message = "adminId不能为空")
    private Integer adminId;

    private String username;

    private String mobile;

    private String email;

    private String avatar;

}
