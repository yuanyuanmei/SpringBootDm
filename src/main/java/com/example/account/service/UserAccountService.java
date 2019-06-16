package com.example.account.service;

import com.alibaba.fastjson.JSONObject;
import com.example.account.beans.UserAccountBean;


public interface UserAccountService {
    //更新账号
    JSONObject save(UserAccountBean paramBean);
}
