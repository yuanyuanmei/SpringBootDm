package com.example.common.aop;

import com.example.common.annotations.CrudCustom;
import com.example.common.base.BaseEntity;
import com.example.common.base.BaseService;
import com.example.common.dto.PageDto;
import com.example.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@Order(2)
public class CrudInterceptor {

    @Before(value = "@annotation(com.example.common.annotations.CrudCustom)")
    public void before(JoinPoint joinPoint) {
        // 获取代理方法
        Method proxyMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // 通过代理方法获取目标方法
        Method targetMethod = null;
        try {
            targetMethod = joinPoint.getTarget().getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //1.获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //2.CRUD操作
        // 通过目标方法获取自定义注解
        CrudCustom crudCustom = targetMethod.getAnnotation(CrudCustom.class);

        //通过注解参数获得目标对象
        BaseService baseService = (BaseService) SpringContextUtil.getApplicationContext().getBean(crudCustom.value());
        //获得请求类型
        String methodType = request.getMethod();
        Long id = Objects.isNull(request.getParameter("id")) ? null:Long.valueOf(request.getParameter("id")) ;
        //根据请求类型区分不同操作
        switch (methodType){
            case "GET":
                Integer pageNum =  Objects.isNull(request.getParameter("pageNum")) ? 1 : Integer.valueOf(request.getParameter("pageNum"));
                Integer pageSize =  Objects.isNull(request.getParameter("pageSize")) ? 10 : Integer.valueOf(request.getParameter("pageSize"));
                if(Objects.isNull(id)){
                    request.setAttribute("jsonObject",baseService.pageList(new PageDto(pageNum,pageSize)));
                }else{
                    request.setAttribute("jsonObject",baseService.selectByPrimaryKey(id));
                }
                break;
            case "POST":
                BaseEntity bean = (BaseEntity) joinPoint.getArgs()[0];
                if(Objects.isNull(bean.getId())){
                    request.setAttribute("jsonObject",baseService.insert(joinPoint.getArgs()[0]));
                }else{
                    request.setAttribute("jsonObject",baseService.update(joinPoint.getArgs()[0]));
                }
                break;
            case "DELETE":
                request.setAttribute("jsonObject",baseService.delete(id));
                break;
        }

    }

}
