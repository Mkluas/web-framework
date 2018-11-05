package cn.mklaus.framework.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.nutz.dao.pager.Pager;

import java.util.List;

/**
 * @author Mklaus
 * Created on 2018-01-08 上午11:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class Pagination extends Pager {

    private List<?> list;

    public Pagination(Pager pager, List<?> list) {
        this.setPageNumber(pager.getPageNumber());
        this.setPageSize(pager.getPageSize());
        this.setRecordCount(pager.getRecordCount());
        this.list = list;
    }

    public static Pagination create(Pager pager, List<?> list) {
        return new Pagination(pager, list);
    }

}
