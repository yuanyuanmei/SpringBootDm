package com.example.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.JobDto;
import com.example.common.service.IJobService;
import com.example.common.util.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @Autowired
    private IJobService jobService;
    @GetMapping("/hello")
    public JSONObject hello() throws SchedulerException {
        JobDto jobDto = new JobDto();
        jobDto.setId(1);
        jobDto.setCron("0/2 * * * * ?");
        jobDto.setJobName("DM");
        jobDto.setSpringBeanName("taskSayHi");
        jobDto.setMethodName("SayHi");
        jobDto.setDescription("123");
        jobDto.setIsSysJob(false);
        jobDto.setStatus(1);
        jobService.saveJob(jobDto);
        return StringUtils.formatSuccessJson("hello");
    }

}
