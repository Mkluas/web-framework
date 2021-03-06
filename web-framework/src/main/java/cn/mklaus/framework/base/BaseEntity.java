package cn.mklaus.framework.base;

import lombok.Data;
import lombok.ToString;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Prev;

/**
 * @author Mklaus
 * Created on 2018-01-08 上午10:51
 */
@Data
@ToString
public class BaseEntity extends Entity<Integer> {

    @Column("ct")
    @ColDefine(update = false, width = 10)
    @Prev(els = @EL("$me.now()"))
    protected Integer createTime;

    @Column("ut")
    @ColDefine(width = 10)
    @Prev(els = @EL("$me.now()"))
    protected Integer updateTime;

    @Override
    public Integer now() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    @Override
    public void refreshUpdateTime() {
        this.updateTime = (int) (System.currentTimeMillis() / 1000);
    }

}
