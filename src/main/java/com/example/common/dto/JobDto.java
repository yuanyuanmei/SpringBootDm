package com.example.common.dto;

import com.example.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto extends BaseEntity<Integer> {

	private static final long serialVersionUID = -2458935535811207209L;

	private String jobName;

	private String description;

	private String cron;

	private String springBeanName;

	private String methodName;

	private Boolean isSysJob;

	private int status;

}
