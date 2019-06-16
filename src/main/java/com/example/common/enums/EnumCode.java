package com.example.common.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 返回码枚举
 */
public enum EnumCode {
    SUCCESS(200,"返回成功"), FAIL(204,"返回失败");

    //枚举的变量类型为final 防止被修改
    private final Integer key;
    private final String value;

    EnumCode(Integer key, String value){
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
    public static List<EnumCode> list = Arrays.asList(values());
}
