package com.elan.bg.utils;

import com.elan.bg.dto.LineDto;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public static <T> List<T> getListObject(T object,String splitChar) {
            Field[] fields = object.getClass().getDeclaredFields();
            List<T> list = new ArrayList<>();
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

        public static <T> void addList(List<T> list,String value,Field field,Object newInstance,int index){
            try {
                Object o = list.size() > 0 && list.size() > index ? list.get(index): newInstance;
                field.set(o,value);
                if (list.size() <= index) list.add((T) o);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    /**
     * 将集合组合成一个对象
     * @param list
     * @param splitChar
     * @param <T>
     * @return
     */
    public static <T> Object getObject(List<T> list,String splitChar){
        if(list.size() == 0){
            return null;
        }
        try {
            Object item = list.get(0).getClass().newInstance();
            Field[] fields = item.getClass().getDeclaredFields();
            for(int i = 0;i< fields.length; i++){
                fields[i].setAccessible(true);
                for(Object obj:list){
                    Field[] objfileds = obj.getClass().getDeclaredFields();
                    for(int j=0;j<objfileds.length;j++){
                        objfileds[j].setAccessible(true);
                        if(fields[i].getName().equals(objfileds[j].getName())){
                            Object value = objfileds[j].get(obj);
                            Object itemvalue = fields[i].get(item);
                            if(value instanceof String){
                                fields[i].set(item,itemvalue == null ? value:itemvalue+splitChar+value);
                            }
                        }
                    }
                }
            }
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

}
