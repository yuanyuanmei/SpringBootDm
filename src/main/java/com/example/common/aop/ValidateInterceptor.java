package com.example.common.aop;

import com.example.common.annotations.ValidateCustom;
import com.example.common.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@Order(1)
/**
 * 拦截自定义验证注解
 */
public class ValidateInterceptor {

    // annotation表达式寻找自定义注解的位置
    @Before(value = "@annotation(com.example.common.annotations.ValidateCustom)")
    public void before(JoinPoint joinPoint) throws Exception {
        // 获取代理方法
        Method proxyMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
        // 通过代理方法获取目标方法
        Method targetMethod = joinPoint.getTarget().getClass().getMethod(proxyMethod.getName(),proxyMethod.getParameterTypes());
        // 通过目标方法获取自定义注解
        ValidateCustom custom = targetMethod.getAnnotation(ValidateCustom.class);
        //jdk8新特性filter过滤属性+三元运算符
        List<Object> objects = Arrays.stream(joinPoint.getArgs())
                .filter(Object -> Object.getClass().equals(custom.value()))
                .collect(Collectors.toList());
        Object obj = objects.size() > 0 ? objects.get(0) : null;

        // 参数校验
        ValidationUtils.validate(obj);

    }
}
