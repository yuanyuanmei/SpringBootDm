package com.example.account.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 用户枚举
 */
public enum EnumUserType {
    COMMON(1,"普通用户"), ADMIN(2,"超级管理员");

    //枚举的变量类型为final 防止被修改
    private final Integer key;
    private final String value;

    EnumUserType(Integer key, String value){
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    //枚举的变量必须为static静态
    public static List<EnumUserType> list = Arrays.asList(values());
}
