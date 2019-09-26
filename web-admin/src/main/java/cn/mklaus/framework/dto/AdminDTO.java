package cn.mklaus.framework.dto;

import cn.mklaus.framework.entity.Admin;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author klaus
 * Created on 2019-09-12 00:10
 */
@Data
@ToString
public class AdminDTO {

    public AdminDTO(Admin admin) {
        this.id = admin.getId();
        this.account = admin.getAccount();
        this.username = admin.getUsername();
        this.mobile = admin.getMobile();
        this.email = admin.getEmail();
        this.avatar = admin.getAvatar();
        this.roles = admin.getRoles();
        this.forbid = admin.getForbid();
        this.createTime = admin.getCreateTime();
        this.updateTime = admin.getUpdateTime();
    }

    private Integer id;

    private String account;

    private String username;

    private String mobile;

    private String email;

    private String avatar;

    private List<String> roles;

    private Boolean forbid;

    private Integer createTime;

    private Integer updateTime;

    public static List<AdminDTO> toList(List adminList) {
        List<AdminDTO> dtoList = new ArrayList<>();
        for(Object o : adminList) {
            dtoList.add(new AdminDTO((Admin) o));
        }
        return dtoList;
    }

}
