package com.example.common.base;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 树结构
 */
@Data
public abstract class BaseTree<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 父级编号
     */
    private Integer parentId;

    /**
     * 子集菜单
     */
    private List<T> childList;

}
