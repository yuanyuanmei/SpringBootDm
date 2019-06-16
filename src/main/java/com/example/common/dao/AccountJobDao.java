package com.example.common.dao;

import com.example.common.base.BaseDao;
import com.example.common.dto.JobDto;

public interface AccountJobDao extends BaseDao<JobDto> {

	JobDto getByName(String name);
}
