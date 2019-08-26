package com.elan.bg.utils;

import com.elan.bg.dto.LineDto;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtil {


    /**
     * 获取对象属性名称
     * @param object
     * @return
     */
    public static String[] getFiledName(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        String[] fieldNamStrings = new String[fields.length];
        for (int i = 0; i < fieldNamStrings.length; i++) {
            fieldNamStrings[i] = fields[i].getName();
        }
        return fieldNamStrings;
    }


    /**
     * 根据对象拆分值为list集合
     * @param object
     * @return
     */
    public static List<Object> getListObject(Object object,String splitChar) {
            Field[] fields = object.getClass().getDeclaredFields();
            List<Object> list = new ArrayList<>();
            try {
                for (int i = 0; i < fields.length; i++) {

                    fields[i].setAccessible(true);
                    Object value = fields[i].get(object);
                    String[] values = new String[0];

                    if (value != null) values = String.valueOf(value).split(splitChar,-1);
                    for (int j = 0; j < values.length; j++) {
                        addList(list,values[j],fields[i],object.getClass().newInstance(),j);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        public static void addList(List<Object> list,String value,Field field,Object newInstance,int index){
            try {
                Object o = list.size() > 0 && list.size() > index ? list.get(index): newInstance;
                field.set(o,value);
                if (list.size() <= index) list.add(o);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

}
