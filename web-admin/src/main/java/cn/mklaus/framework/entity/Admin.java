package cn.mklaus.framework.entity;

import cn.mklaus.framework.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

import java.util.List;

/**
 * @author klaus
 * Created on 2019-09-11 17:46
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Table("t_admin")
public class Admin extends BaseEntity {

    @Column
    @ColDefine(update = false)
    private String account;

    @Column
    @Default("")
    private String password;

    @Column
    @ColDefine(update = false)
    private String salt;

    @Column
    @Default("")
    private String username;

    @Column
    @Default("")
    private String mobile;

    @Column
    @Default("")
    private String email;

    @Column
    @Default("")
    private String avatar;

    @Column
    @Default("[]")
    private List<String> roles;

    @Column
    @Default("0")
    private Boolean forbid;
}
