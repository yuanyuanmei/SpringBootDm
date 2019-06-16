package com.example.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.account.beans.UserBaseBean;
import com.example.account.dao.UserBaseDao;
import com.example.account.service.UserBaseService;
import com.example.common.base.BaseServiceImpl;
import com.example.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户基础业务实现类
 */
@Service
public class UserBaseServiceImpl extends BaseServiceImpl<UserBaseBean> implements UserBaseService {

    @Autowired
    private UserBaseDao baseDao;

    @Transactional
    @Override
    public JSONObject insert(UserBaseBean bean){
        //生成7位随机数用户名
        bean.setUsername(StringUtils.getItemID(7));
        return StringUtils.formatSuccessJson("新增成功！",baseDao.insert(bean));
    }

}
