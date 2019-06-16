package com.example.account.dao;

import com.example.account.beans.UserAccountBean;
import com.example.common.base.BaseDao;

public interface UserAccountDao extends BaseDao<UserAccountBean> {

    /**
     * 根据帐号查询
     */
    UserAccountBean findByAccount(String account);

}