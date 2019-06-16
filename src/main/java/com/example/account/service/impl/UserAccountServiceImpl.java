package com.example.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.account.beans.UserAccountBean;
import com.example.account.beans.UserBaseBean;
import com.example.account.dao.UserAccountDao;
import com.example.account.dao.UserBaseDao;
import com.example.account.service.UserAccountService;
import com.example.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountDao accountDao;

    @Autowired
    private UserBaseDao baseDao;

    @Transactional
    @Override
    public JSONObject save(UserAccountBean paramBean){
        int result = 0;
        String msg = "";
        //根据帐号查找是否存在
        if(Objects.nonNull(accountDao.findByAccount(paramBean.getAccount()))){
            return StringUtils.formatFailJson("账号已存在");
        }
        if(Objects.isNull(paramBean.getId())){
            //生成7位数随机用户名
            String username = StringUtils.getItemID(7);
            UserBaseBean baseBean = new UserBaseBean(username);
            //新增用户基础表
            baseDao.insert(baseBean);
            //获取usr_id
            paramBean.setUserId(baseBean.getId());
            //密码加盐,md5加密
            String salt = StringUtils.getItemID(4);
            paramBean.setSalt(salt);
            paramBean.setOpenPwd(paramBean.getPassword());
            paramBean.setPassword(StringUtils.md5(paramBean.getPassword(),salt));
            //新增用户帐号表
            result = accountDao.insert(paramBean);
            msg = "新增成功";
        }else{
            result = accountDao.update(paramBean);
            msg = "更新成功";
        }
        if(result > 0){
            return StringUtils.formatSuccessJson(msg,paramBean);
        }
        return StringUtils.formatFailJson("更新失败");
    }


}
