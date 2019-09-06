package cn.mklaus.framework.base;

import cn.mklaus.framework.bean.Pagination;
import cn.mklaus.framework.util.Times;
import org.nutz.dao.*;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

    protected String wrapSearchKey(String key) {
        return "%" + key + "%";
    }

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
    public T fetch(int id) {
        return this.dao().fetch(this.getEntityClass(), id);
    }

    public T fetch(String name) {
        return this.dao().fetch(this.getEntityClass(), name);
    }

    @Override
    public T fetch(Condition condition) {
        return this.dao().fetch(this.getEntityClass(), condition);
    }

    @Override
    public List<T> query(Condition condition) {
        return this.dao().query(this.getEntityClass(), condition);
    }

    @Override
    public List<T> query(Condition condition, Pager pager) {
        return this.dao().query(this.getEntityClass(), condition, pager);
    }

    public List<T> query(Condition condition, FieldMatcher fieldMatcher, Pager pager) {
        return this.dao().query(this.getEntityClass(), condition, pager, fieldMatcher);
    }

    public List<T> queryAll() {
        return this.dao().query(this.getEntityClass(), null, null);
    }

    public T getField(String fieldName, int id) {
        return Daos.ext(this.dao(), FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), id);
    }

    public T getField(String fieldName, String name) {
        return Daos.ext(this.dao(), FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), name);
    }

    public T getField(String fieldName, Condition condition) {
        return Daos.ext(this.dao(), FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), condition);
    }

    public int getMaxId() {
        return this.dao().getMaxId(this.getEntityClass());
    }


    private static final String GROUP_BY = "group by";
    private static final String ORDER_BY = "order by";
    public int count(Sql sql) {
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

    @Override
    public int count(Condition condition) {
        return this.dao().count(getEntityClass(), condition);
    }

    public List<Record> list(Sql sql) {
        sql.setCallback(Sqls.callback.records());
        this.dao().execute(sql);
        return sql.getList(Record.class);
    }

    public Pagination listPage(Sql sql, Pager pager) {
        int count = this.count(sql);
        pager.setRecordCount(count);
        sql.setPager(pager);
        List<Record> recordList = (count == 0) ? new ArrayList<>() : list(sql);
        return Pagination.create(pager, recordList);
    }

    public Pagination listPage(Condition condition, Pager pager) {
        int count = this.count(condition);
        pager.setRecordCount(count);
        List<T> recordList = (count == 0) ? new ArrayList<>() : query(condition, pager);
        return Pagination.create(pager, recordList);
    }

    public Pagination listPage(Condition condition, FieldMatcher fieldMatcher, Pager pager) {
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
    public T insert(T t) {
        return this.dao().insert(t);
    }

    public T fastInsert(T t) {
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
     * @return 成功条数
     */
    public int update(T obj) {
        obj.refreshUpdateTime();
        return this.dao().update(obj);
    }

    public int update(T obj, FieldFilter fieldFilter) {
        obj.refreshUpdateTime();
        return this.dao().update(obj, fieldFilter);
    }

    @Override
    public int update(Chain chain, Condition condition) {
        return this.dao().update(this.getEntityClass(), chain, condition);
    }

    public int updateIgnoreNull(T obj) {
        obj.refreshUpdateTime();
        return this.dao().updateIgnoreNull(obj);
    }

    public int updateWithVersion(T obj) {
        obj.refreshUpdateTime();
        return this.dao().updateWithVersion(obj);
    }

    public int updateWithVersion(T obj, FieldFilter fieldFilter) {
        obj.refreshUpdateTime();
        return this.dao().updateWithVersion(obj, fieldFilter);
    }


    /**
     *=====================================================
     *      DELETE
     *=====================================================
     */

    /**
     * delete
     * @param id id
     * @return int
     */
    public int delete(int id) {
        return this.dao().delete(getEntityClass(), id);
    }

    public int delete(Object obj) {
        return this.dao().delete(obj);
    }

    @Override
    public int clear(Condition condition) {
        return this.dao().clear(this.getEntityClass(), condition);
    }

}
