package cn.mklaus.framework.base;

import cn.mklaus.framework.bean.Pagination;
import cn.mklaus.framework.bean.ServiceResult;
import org.nutz.dao.*;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mklaus
 * Created on 2018-01-08 上午11:26
 */
public interface BaseService<T extends Entity> {

    Dao dao();

    Class<T> getEntityClass();

    /**
     *=====================================================
     *      SELECT
     *=====================================================
     */

    /**
     * fetch
     * @param id id
     * @return T
     */
    default T get(int id) {
        return fetch(id);
    }

    default T checkGet(int id) {
        T object = fetch(id);
        Assert.notNull(object, getEntityClass().getSimpleName() + " not exist");
        return object;
    }

    default T fetch(int id) {
        return this.dao().fetch(this.getEntityClass(), id);
    }

    default T fetch(String name) {
        return this.dao().fetch(this.getEntityClass(), name);
    }

    default T fetch(Condition condition) {
        return this.dao().fetch(this.getEntityClass(), condition);
    }

    default List<T> query(Condition condition) {
        return this.dao().query(this.getEntityClass(), condition);
    }

    default List<T> query(Condition condition, Pager pager) {
        return this.dao().query(this.getEntityClass(), condition, pager);
    }

    default List<T> query(Condition condition, FieldMatcher fieldMatcher, Pager pager) {
        return this.dao().query(this.getEntityClass(), condition, pager, fieldMatcher);
    }

    default List<T> queryAll() {
        return this.dao().query(this.getEntityClass(), null, null);
    }

    default int getMaxId() {
        return this.dao().getMaxId(this.getEntityClass());
    }

    String GROUP_BY = "group by";
    String ORDER_BY = "order by";

    default int count(Sql sql) {
        String sourceSql = sql.getSourceSql();
        int fromIndex = sourceSql.toLowerCase().lastIndexOf("from");
        sourceSql = "SELECT COUNT(*) " + sourceSql.substring(fromIndex);
        String lowerCaseSql = sourceSql.toLowerCase();
        int endPoint = sourceSql.length();

        if (lowerCaseSql.contains(GROUP_BY)) {
            endPoint = lowerCaseSql.lastIndexOf(GROUP_BY);
        }

        if (endPoint == sourceSql.length() && sourceSql.toLowerCase().contains(ORDER_BY)) {
            endPoint = lowerCaseSql.lastIndexOf(ORDER_BY);
        }

        if (endPoint != sourceSql.length()) {
            sourceSql = sourceSql.substring(0, endPoint);
        }

        Sql countSql = Sqls.create(sourceSql);
        sql.params().keys().forEach(key -> countSql.setParam(key, sql.params().get(key)));

        countSql.setCallback((conn, rs, executeSql) -> {
            int result = 0;
            if (rs != null && rs.next()) {
                result = rs.getInt(1);
            }
            return result;
        });

        this.dao().execute(countSql);
        return countSql.getInt();
    }

    default int count(Condition condition) {
        return this.dao().count(getEntityClass(), condition);
    }

    default List<Record> list(Sql sql) {
        sql.setCallback(Sqls.callback.records());
        this.dao().execute(sql);
        return sql.getList(Record.class);
    }

    default Pagination listPage(Sql sql, Pager pager) {
        int count = this.count(sql);
        pager.setRecordCount(count);
        sql.setPager(pager);
        List<Record> recordList = (count == 0) ? new ArrayList<>() : list(sql);
        return Pagination.create(pager, recordList);
    }

    default Pagination listPage(Condition condition, Pager pager) {
        int count = this.count(condition);
        pager.setRecordCount(count);
        List<T> recordList = (count == 0) ? new ArrayList<>() : query(condition, pager);
        return Pagination.create(pager, recordList);
    }

    default Pagination listPage(Condition condition, FieldMatcher fieldMatcher, Pager pager) {
        int count = this.count(condition);
        pager.setRecordCount(count);
        List<T> recordList = (count == 0) ? new ArrayList<>() : query(condition, fieldMatcher, pager);
        return Pagination.create(pager, recordList);
    }

    /**
     *=====================================================
     *      INSERT
     *=====================================================
     */

    /**
     * insert
     * @param t t
     * @return T
     */
    default T insert(T t) {
        return this.dao().insert(t);
    }

    default T fastInsert(T t) {
        return this.dao().fastInsert(t);
    }

    /**
     *=====================================================
     *      UPDATE
     *=====================================================
     */

    /**
     * update
     * @param obj obj
     * @return int 更新条数
     */
    default int update(T obj) {
        obj.refreshUpdateTime();
        return this.dao().update(obj);
    }

    default int update(T obj, FieldFilter fieldFilter) {
        obj.refreshUpdateTime();
        return this.dao().update(obj, fieldFilter);
    }

    default int update(Chain chain, Condition condition) {
        return this.dao().update(this.getEntityClass(), chain, condition);
    }

    default int updateIgnoreNull(T obj) {
        obj.refreshUpdateTime();
        return this.dao().updateIgnoreNull(obj);
    }

    default int updateWithVersion(T obj) {
        obj.refreshUpdateTime();
        return this.dao().updateWithVersion(obj);
    }

    default int updateWithVersion(T obj, FieldFilter fieldFilter) {
        obj.refreshUpdateTime();
        return this.dao().updateWithVersion(obj, fieldFilter);
    }

    /**
     *=====================================================
     *      DELETE
     *=====================================================
     */

    default int delete(int id) {
        return this.dao().delete(getEntityClass(), id);
    }

    default int delete(Object obj) {
        return this.dao().delete(obj);
    }

    default int clear(Condition condition) {
        return this.dao().clear(this.getEntityClass(), condition);
    }

    default ServiceResult remove(int id) {
        T object = checkGet(id);
        delete(object);
        return ServiceResult.ok();
    }

}
