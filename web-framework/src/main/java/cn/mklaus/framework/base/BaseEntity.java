package cn.mklaus.framework.base;

import lombok.Data;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Default;

/**
 * @author Mklaus
 * Created on 2018-01-08 上午10:51
 */
@Data
@ToString
public class BaseEntity {

    @Id
    protected Integer id;

    @Column("ct")
    @ColDefine(update = false, width = 10)
    @Prev(els = @EL("$me.now()"))
    protected Integer createTime;

    @Column("ut")
    @ColDefine(width = 10)
    @Prev(els = @EL("$me.now()"))
    protected Integer updateTime;

    @Column(version = true)
    @Default("0")
    private Integer version;

    public int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }

}
