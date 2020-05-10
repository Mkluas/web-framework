package cn.mklaus.framework.base;

import org.nutz.dao.*;
import org.nutz.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

/**
 * @author Mklaus
 * Created on 2018-01-08 上午11:26
 */
public abstract class BaseServiceImpl<T extends Entity> extends EntityService<T> {

    @Autowired
    private Dao dao;

    @PostConstruct
    public void init() {
        this.setDao(dao);
    }

}
