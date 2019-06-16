package com.example.common.service;

import com.example.common.dto.JobDto;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

public interface IJobService {

    void saveJob(JobDto jobDto);

    void doJob(JobDataMap jobDataMap);

    void deleteJob(Long id) throws SchedulerException;

}
