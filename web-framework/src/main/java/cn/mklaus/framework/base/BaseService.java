package cn.mklaus.framework.base;

import cn.mklaus.framework.bean.Pagination;
import org.nutz.dao.*;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;

import java.util.List;

/**
 * @author Mklaus
 * Created on 2018-01-08 上午11:26
 */
public interface BaseService<T> {

    Dao dao();

    /**
     *=====================================================
     *      SELECT
     *=====================================================
     */

    T fetch(int id);

    T fetch(String name);

    T fetch(Condition condition);

    List<T> query(Condition condition);

    List<T> query(Condition condition, Pager pager);

    List<T> query(Condition condition, FieldMatcher fieldMatcher, Pager pager);

    List<T> queryAll();

    T getField(String fieldName, int id);

    T getField(String fieldName, String name);

    T getField(String fieldName, Condition condition);

    int getMaxId();

    int count(Sql sql);

    int count(Condition condition);

    List<Record> list(Sql sql);

    Pagination listPage(Sql sql, Pager pager);

    Pagination listPage(Condition condition, Pager pager);

    Pagination listPage(Condition condition, FieldMatcher fieldMatcher, Pager pager);

    /**
     *=====================================================
     *      INSERT
     *=====================================================
     */

    T insert(T t);

    T fastInsert(T t);

    /**
     *=====================================================
     *      UPDATE
     *=====================================================
     */

    int update(T obj);

    int update(T obj, FieldFilter fieldFilter);

    int update(Chain chain, Condition condition);

    int updateIgnoreNull(T obj);

    int updateWithVersion(T obj);

    int updateWithVersion(T obj, FieldFilter fieldFilter);

    /**
     *=====================================================
     *      DELETE
     *=====================================================
     */

    int delete(int id);

    int delete(Object obj);

    int clear(Condition condition);

}
