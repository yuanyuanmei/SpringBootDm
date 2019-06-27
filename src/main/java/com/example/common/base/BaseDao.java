package com.example.common.base;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 接口基类，实现增删改查操作
 * @param <T>
 */
public interface BaseDao<T> {

    /**
     * 根据ID查询
     * @param id
     * @return
     */

    T selectByPrimaryKey(Long id);

    @SelectProvider(method = "selectByPrimaryKey", type = BaseSqlProvider.class)
    T selectByPrimaryKey123(T t);

    /**
     * 软删除
     * @param id
     * @return
     */
    int delete(Long id);


    @DeleteProvider(method = "delete", type = BaseSqlProvider.class)
    int delete123(T t);
    /**
     * 单表新增
     * @param t
     * @return
     */
    @InsertProvider(method = "insert", type=BaseSqlProvider.class)
    @Options(useGeneratedKeys = true)
    int insert(T t);

    /**
     * 单表修改
     * @param t
     * @return
     */
    @UpdateProvider(method = "update", type = BaseSqlProvider.class)
    int update(T t);

    /**
     * 查询集合
     * @return
     */
    List<T> list();

}
