package com.example.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.JobDto;
import com.example.common.service.IJobService;
import com.example.common.util.ExcelUtil;
import com.example.common.util.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/excel")
    public JSONObject excel(HttpServletResponse response){
        String[] headers = new String[]{"a","b","c"};
        List<Object[]> list = new ArrayList<>();
        String[] s1 = new String[]{"1","2","3"};
        String[] s2 = new String[]{"4","5","6"};
        String[] s3 = new String[]{"7","8","9"};
        String[] s4 = new String[]{"10","11","12"};
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        ExcelUtil.excelExport("ddd",headers,list,response);
        return StringUtils.formatSuccessJson("hello");
    }

}
