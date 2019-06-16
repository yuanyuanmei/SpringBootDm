package com.example.common.service.impl;

import com.example.common.dao.AccountJobDao;
import com.example.common.dto.JobDto;
import com.example.common.dto.SpringBeanJob;
import com.example.common.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class JobServiceImpl implements IJobService {

	@Autowired
	private Scheduler scheduler;
	@Autowired
	private ApplicationContext applicationContext;

	private static final String JOB_DATA_KEY = "JOB_DATA_KEY";

	@Autowired
	private AccountJobDao accountJobDao;

	@Override
	public void saveJob(JobDto jobDto) {
		checkJobDto(jobDto);
		String name = jobDto.getJobName();
		JobKey jobKey = JobKey.jobKey(name);
		/**
		 * JobDetail
		 * 表示一个具体的可执行的调度程序，
		 * Job 是这个可执行程调度程序所要执行的内容，
		 * 另外 JobDetail 还包含了这个任务调度的方案和策略。
		 */
		JobDetail jobDetail = JobBuilder
									.newJob(SpringBeanJob.class)
									.storeDurably()
									.withDescription(jobDto.getDescription())
									.withIdentity(jobKey).build();

		jobDetail.getJobDataMap().put(JOB_DATA_KEY, jobDto);
		jobDetail.getJobDataMap().put("methodName", jobDto.getMethodName());
		jobDetail.getJobDataMap().put("beanName", jobDto.getSpringBeanName());

		//任务调度容器
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobDto.getCron());

		/**
		 * 代表一个调度参数的配置，什么时候去调。
		 */
		CronTrigger cronTrigger = TriggerBuilder
									.newTrigger()
									.withIdentity(name)
									.withSchedule(cronScheduleBuilder)
									.forJob(jobKey).build();

		try {
			boolean exists = scheduler.checkExists(jobKey);
			if (exists) {
				scheduler.rescheduleJob(new TriggerKey(name), cronTrigger);
				scheduler.addJob(jobDetail, true);
			} else {
				scheduler.scheduleJob(jobDetail, cronTrigger);
			}

			JobDto model = accountJobDao.getByName(name);

			if (model == null) {
				accountJobDao.insert(jobDto);
			} else {
				accountJobDao.update(jobDto);
			}
		} catch (SchedulerException e) {
			log.error("新增或修改job异常", e);
		}
	}

	private void checkJobDto(JobDto jobDto) {
		String springBeanName = jobDto.getSpringBeanName();
		boolean flag = applicationContext.containsBean(springBeanName);
		if (!flag) {
			throw new IllegalArgumentException("bean：" + springBeanName + "不存在，bean名如userServiceImpl,首字母小写");
		}

		Object object = applicationContext.getBean(springBeanName);
		Class<?> clazz = object.getClass();
		if (AopUtils.isAopProxy(object)) {
			clazz = clazz.getSuperclass();
		}

		String methodName = jobDto.getMethodName();
		Method[] methods = clazz.getDeclaredMethods();

		Set<String> names = new HashSet<>();
		Arrays.asList(methods).parallelStream().forEach(m -> {
			Class<?>[] classes = m.getParameterTypes();
			if (classes.length == 0) {
				names.add(m.getName());
			}
		});

		if (names.size() == 0) {
			throw new IllegalArgumentException("该bean没有无参方法");
		}

		if (!names.contains(methodName)) {
			throw new IllegalArgumentException("未找到无参方法" + methodName + ",该bean所有方法名为：" + names);
		}
	}

	@Override
	public void doJob(JobDataMap jobDataMap) {
		//JobDto jobDto = (JobDto) jobDataMap.get(JOB_DATA_KEY);
		//String beanName = jobDto.getSpringBeanName();
		//String methodName = jobDto.getMethodName();
		String beanName = (String) jobDataMap.get("beanName");
		String methodName = (String) jobDataMap.get("methodName");
		Object object = applicationContext.getBean(beanName);

		try {
			log.info("job:bean：{}，方法名：{}", beanName, methodName);
			Method method = object.getClass().getDeclaredMethod(methodName);
			method.invoke(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除job
	 * 
	 * @throws SchedulerException
	 */
	@Override
	public void deleteJob(Long id) throws SchedulerException {
		JobDto jobDto = accountJobDao.selectByPrimaryKey(id);

		if (jobDto.getIsSysJob() != null && jobDto.getIsSysJob()) {
			throw new IllegalArgumentException("该job是系统任务，不能删除，因为此job是在代码里初始化的，删除该类job请先确保相关代码已经去除");
		}

		String jobName = jobDto.getJobName();
		JobKey jobKey = JobKey.jobKey(jobName);

		scheduler.pauseJob(jobKey);
		scheduler.unscheduleJob(new TriggerKey(jobName));
		scheduler.deleteJob(jobKey);

		jobDto.setStatus(0);
		accountJobDao.update(jobDto);
	}

}
