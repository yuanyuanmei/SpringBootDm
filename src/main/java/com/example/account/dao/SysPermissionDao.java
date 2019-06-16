package com.example.account.dao;

import com.example.account.beans.SysPermissionBean;
import com.example.common.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限接口
 */
public interface SysPermissionDao extends BaseDao<SysPermissionBean> {

    /**
     * 根据角色ID查询权限集合
     * @param roleId
     * @return
     */
    List<SysPermissionBean> getPermissionsByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据用户ID查询权限集合
     */
    List<SysPermissionBean> getPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据URL查询权限
     */
    SysPermissionBean getPermsBeanByUrl(@Param("url") String url);
}