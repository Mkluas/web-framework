package cn.mklaus.framework.bean;

import lombok.Data;
import lombok.ToString;
import org.nutz.dao.pager.Pager;

/**
 * @author Klaus
 * @date 2018/7/7
 */
@Data
@ToString
public class PageVO {

    private String key;

    private Integer pageNumber;

    private Integer pageSize;

    public PageVO() {
        this.pageNumber = 1;
        this.pageSize = 10;
        this.key = "";
    }

    public PageVO(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.key = "";
    }

    public Pager toPager() {
        return new Pager(pageNumber, pageSize);
    }

}
