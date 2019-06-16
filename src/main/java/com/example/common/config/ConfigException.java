package com.example.common.config;

import com.example.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ConfigException {

    //捕获全局异常,处理所有不可知的异常
    @ExceptionHandler(value=Exception.class)
    Object handleException(Exception e, HttpServletRequest request){
        log.error("url {}, msg {}",request.getRequestURL(), e.getMessage());
        return StringUtils.formatFailJson(e.getMessage(),request.getRequestURI());
    }

}
