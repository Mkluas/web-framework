package cn.mklaus.framework.vo;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author klaus
 * @date 2019-09-12 00:51
 */
@ApiModel
@Data
@ToString
public class AdminRolesVO {

    @ApiModelProperty(value = "adminId", required = true, example = "1")
    @NotNull(message = "adminId不能为空")
    private Integer adminId;

    @ApiModelProperty(value = "roles数组，例: [\"admin\", \"editor\"]。", required = true)
    private JSONArray roles;

    public List<String> getRoles() {
        return roles.toJavaList(String.class);
    }

}
