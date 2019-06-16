package com.example.account.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限枚举
 */
public enum  EnumPermsType {

    MENU(1,"菜单"), FUNC(2,"功能");

    //枚举的变量类型为final 防止被修改
    private final Integer key;
    private final String value;

    EnumPermsType(Integer key, String value){
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
    public static List<EnumPermsType> list = Arrays.asList(values());

    public static String keyOf(Integer key){
        List<EnumPermsType> collect = list.stream().filter(item -> item.key.equals(key)).collect(Collectors.toList());
        String value = collect.size() > 0 ? collect.get(0).getValue() : "";
        return value;
    }

}
