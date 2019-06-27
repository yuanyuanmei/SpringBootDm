package com.example.common.base;

import com.example.common.annotations.sql.Column;
import com.example.common.annotations.sql.Exclude;
import com.example.common.annotations.sql.Id;
import com.example.common.annotations.sql.Table;
import com.example.common.util.ColumnUtil;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 公共增删改查
 * @param <T>
 */
public class BaseSqlProvider<T> {

    /**
     * 新增方法
     * @param bean
     * @return
     */
    @Options
    public String insert(T bean){
        SQL sql = new SQL();
        //1. 通过反射获得目标对象
        Class clazz = bean.getClass();
        //2. 通过注解获得对象表名
        Table tableName = (Table) clazz.getAnnotation(Table.class);
        //调用新增方法
        sql.INSERT_INTO(tableName.name());
        //3. 通过反射获得所有属性
        List<Field> fields = getFields(clazz);
        //4. 遍历属性，循环插入变量和值
        fields.forEach(item->{
            item.setAccessible(true);
            String column = Objects.isNull(item.getAnnotation(Column.class))? item.getName(): item.getAnnotation(Column.class).name();
            try {
                if(Objects.nonNull(item.get(bean))){
                    sql.VALUES(ColumnUtil.humpToLine(column),String.format("#{"+column+",jdbcType=VARCHAR}"));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return sql.toString();
    }

    /**
     * 软删除
     * @param bean
     * @return
     */
    public String delete(T bean){
        SQL sql = new SQL();
        Class clazz = bean.getClass();
        Table tableName = (Table) clazz.getAnnotation(Table.class);
        //调用删除方法
        sql.DELETE_FROM(tableName.name());

        Field pkField = getPrimaryKeyField(clazz);
        //追加ID条件语句
        sql = addIdWhere(clazz,sql);
        return sql.toString();
    }

    /**
     * 修改方法
     * @param bean
     * @return
     */
    public String update(T bean){
        SQL sql = new SQL();
        Class clazz = bean.getClass();
        Table tableName = (Table) clazz.getAnnotation(Table.class);
        //调用修改方法
        sql.UPDATE(tableName.name());
        //4. 遍历属性，循环插入变量和值
        List<Field> fields = getFields(clazz);
        for (Field item : fields) {
            item.setAccessible(true);
            String column = Objects.isNull(item.getAnnotation(Column.class))? item.getName(): item.getAnnotation(Column.class).name();
            try {
                if(Objects.nonNull(item.get(bean)) && !column.equals("id")){
                    sql.SET(ColumnUtil.humpToLine(column)+"="+String.format("#{"+column+",jdbcType=VARCHAR}"));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sql = addIdWhere(clazz, sql);
        return sql.toString();
    }

    /**
     * 根据ID查询
     * @param bean
     * @return
     */
    public String selectByPrimaryKey(T bean){
        SQL sql = new SQL();
        Class clazz = bean.getClass();
        Table tableName = (Table) clazz.getAnnotation(Table.class);

        sql.SELECT("*").FROM(tableName.name());

        //追加ID条件语句
        sql = addIdWhere(clazz,sql);
        return sql.toString();
    }

    private Field getPrimaryKeyField(Class clazz) {
        List<Field> primaryKeyList = new ArrayList<>();
        List<Field> fields = getFields(clazz);
        fields.forEach(item->{
            item.setAccessible(true);
            if(Objects.nonNull(item.getAnnotation(Id.class))){
                primaryKeyList.add(item);
            }
        });
        return primaryKeyList.size()>0? primaryKeyList.get(0):null;
    }

    /**
     * 通过反射获得对象属性
     * @param clazz
     * @return
     * getFields()    获取所有public字段,包括父类字段
     * getDeclaredFields()	获取所有字段,public和protected和private,但是不包括父类字段
     */
    private List<Field> getFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (Objects.nonNull(clazz)){
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                Exclude key = field.getAnnotation(Exclude.class);
                if(key == null){
                    fieldList.add(field);
                }
            }
            //递归获取父类的属性
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 追加ID条件语句
     * @return
     */
    private SQL addIdWhere(Class clazz,SQL sql){
        Field pkField = getPrimaryKeyField(clazz);
        if(Objects.nonNull(pkField)){
            pkField.setAccessible(true);
            sql.WHERE(pkField.getName()+"="+String.format("#{"+pkField.getName()+"}"));
        }else{
            sql.WHERE(" 1=2");
            throw new RuntimeException("对象中未包含PrimaryKey属性");
        }
        return sql;
    }
}
