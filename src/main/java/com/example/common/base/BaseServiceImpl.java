package com.example.common.base;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.PageDto;
import com.example.common.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private BaseDao<T> baseDao;

    @Override
    public JSONObject selectByPrimaryKey(Long id){
        return StringUtils.formatSuccessJson(baseDao.selectByPrimaryKey(id));
    }

    @Override
    public JSONObject pageList(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPageNum(),pageDto.getPageSize());
        List<T> list = baseDao.list();
        return StringUtils.formatSuccessJson(new PageInfo<>(list));
    }

    @Transactional
    @Override
    public JSONObject delete(Long id){
        return StringUtils.formatSuccessJson("删除成功！",baseDao.delete(id));
    }

    @Transactional
    @Override
    public JSONObject insert(T t){
        return StringUtils.formatSuccessJson("新增成功！",baseDao.insert(t));
    }

    @Transactional
    @Override
    public JSONObject update(T t){
        return StringUtils.formatSuccessJson("修改成功！",baseDao.update(t));
    }

}
