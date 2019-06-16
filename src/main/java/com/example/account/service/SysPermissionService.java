package com.example.account.service;

import com.example.account.beans.SysPermissionBean;
import com.example.common.base.BaseService;

import java.util.List;

/**
 * 权限业务处理
 */
public interface SysPermissionService extends BaseService<SysPermissionBean> {
  /**
   * 获取面包屑
   * @param permsBeanList
   * @param paramBean
   * @return
   */
  List<SysPermissionBean> breadCrumbs(List<SysPermissionBean> templist, List<SysPermissionBean> permsBeanList, SysPermissionBean paramBean);

  /**
   * 根据url查找
   * @param url
   * @return
   */
  SysPermissionBean getPermsBeanByUrl(String url);


}
