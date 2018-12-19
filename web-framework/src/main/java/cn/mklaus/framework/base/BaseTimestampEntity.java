package cn.mklaus.framework.base;

import lombok.Data;
import lombok.ToString;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Prev;

import java.sql.Timestamp;


/**
 * @author klaus
 * @date 2018/12/19 9:44 AM
 */
@Data
@ToString
public class BaseTimestampEntity extends Entity<Timestamp> {

    @Column("ct")
    @ColDefine(update = false, customType = "datetime")
    @Prev(els = @EL("$me.now()"))
    protected Timestamp createTime;

    @Column("ut")
    @ColDefine(customType = "datetime")
    @Prev(els = @EL("$me.now()"))
    protected Timestamp updateTime;

    @Override
    public Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    @Override
    public void refreshUpdateTime() {
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }

}
