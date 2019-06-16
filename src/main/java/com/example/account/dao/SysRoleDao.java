package com.example.account.dao;

import com.example.account.beans.SysRoleBean;
import com.example.common.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleDao extends BaseDao<SysRoleBean> {

    /**
     * 根据用户ID查询角色集合
     * @param userId
     * @return
     */
    List<SysRoleDao> getRolesByUserId(@Param("userId") Long userId);


}