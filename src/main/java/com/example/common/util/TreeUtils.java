package com.example.common.util;

import com.example.common.base.BaseTree;

import java.util.ArrayList;
import java.util.List;

/**
 * 树结构工具类
 * @param <T>
 */
public class TreeUtils<T> {

    public static <T> List<T> convertToTree(List<T> treeList, Integer parentId){
        //声明的接收Tree树结构集合
        List<T> templist = new ArrayList<>();
        for(int i= 0;treeList!=null && i<treeList.size();i++){
            //从集合中获取第i个对象
            BaseTree bean = (BaseTree) treeList.get(i);
            if(bean.getParentId().equals(parentId)){
                bean.setChildList(convertToTree(treeList,bean.getId()));
                templist.add(treeList.get(i));
            }
        }
        return templist;
    }
}
