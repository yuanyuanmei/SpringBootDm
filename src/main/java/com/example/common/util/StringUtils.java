package com.example.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.enums.EnumCode;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.Objects;
import java.util.Random;

public class StringUtils {


    public static final String MD5 = "MD5";

    //加密次数
    public static final Integer encryNum = 1024;

    /**
     * 生成随机数当作getItemID
     * n ： 需要的长度
     * @return
     */
    public static String getItemID( int n )
    {
        String val = "";
        Random random = new Random();
        for ( int i = 0; i < n; i++ )
        {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) )
            { // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val += (char) ( nextInt + random.nextInt( 26 ) );
            }
            else if ( "num".equalsIgnoreCase( str ) )
            { // 产生数字
                val += String.valueOf( random.nextInt( 10 ) );
            }
        }
        return val;
    }

    public static String md5(String code,String salt){
        //加密方式、密码、盐值、加密次数
        return String.valueOf(new SimpleHash(MD5, code, ByteSource.Util.bytes(salt), encryNum));
    }

    private static Object convertToJson(Object data) {
        return !(data instanceof JSONObject) && !(data instanceof JSONArray) ? JSON.toJSON(data) : data;
    }
    /**
     * 自定义JSON数据
     */
    public static JSONObject formatCommonJson(int code, String msg, Object info){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        if(Objects.nonNull(info)){
            jsonObject.put("info",convertToJson(info));
        }
        return jsonObject;
    }

    public static JSONObject formatCommonJson(int code, String msg){
        return StringUtils.formatCommonJson(code,msg,null);
    }

    /**
     * 成功JSON数据
     */
    public static JSONObject formatSuccessJson(String msg, Object info){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", EnumCode.SUCCESS.getKey());
        jsonObject.put("msg",msg);
        if(Objects.nonNull(info)){
            jsonObject.put("info", convertToJson(info));
        }
        return jsonObject;
    }

    public static JSONObject formatSuccessJson(String msg){
        return StringUtils.formatSuccessJson(msg,null);
    }

    public static JSONObject formatSuccessJson(Object info){
        return StringUtils.formatSuccessJson(EnumCode.SUCCESS.getValue(),convertToJson(info));
    }

    /**
     * 失败JSON数据
     */
    public static JSONObject formatFailJson(String msg, Object info){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", EnumCode.FAIL.getKey());
        jsonObject.put("msg",msg);
        if(Objects.nonNull(info)){
            jsonObject.put("info",convertToJson(info));
        }
        return jsonObject;
    }

    public static JSONObject formatFailJson(String msg){
        return StringUtils.formatFailJson(msg,null);
    }

    public static JSONObject formatFailJson(Object info){
        return StringUtils.formatSuccessJson(EnumCode.FAIL.getValue(),convertToJson(info));
    }
}
