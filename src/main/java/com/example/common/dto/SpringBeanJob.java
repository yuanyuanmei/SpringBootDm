package com.example.common.dto;

import com.example.common.config.ConfigJob;
import com.example.common.service.IJobService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SpringBeanJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext()
					.get(ConfigJob.KEY);
			IJobService jobService = applicationContext.getBean(IJobService.class);
			jobService.doJob(context.getJobDetail().getJobDataMap());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
