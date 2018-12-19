package cn.mklaus.framework.base;

import lombok.Data;
import lombok.ToString;
import org.nutz.dao.entity.annotation.*;

/**
 * @author klaus
 * @date 2018/12/19 9:36 AM
 */
@Data
@ToString
public abstract class Entity<T> {

    @Id
    protected Integer id;

    @Column(version = true)
    @Default("0")
    private Integer version;

    public abstract T now();

    /**
     * 刷新更新时间
     */
    public abstract void refreshUpdateTime();

}
