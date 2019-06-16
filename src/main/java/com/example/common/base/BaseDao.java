package com.example.common.base;

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

    /**
     * 软删除
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 单表新增
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 单表修改
     * @param t
     * @return
     */
    int update(T t);

    /**
     * 查询集合
     * @return
     */
    List<T> list();

}
