package com.example.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring项目启动时会初始化加载一遍，我们只需调用该对象即可获得spring容器中任意对象
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

        private static ApplicationContext applicationContext; // Spring应用上下文环境

        /*
         * 实现了ApplicationContextAware 接口，必须实现该方法；
         *通过传递applicationContext参数初始化成员变量applicationContext
         */
        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            SpringContextUtil.applicationContext = applicationContext;
        }

        public static ApplicationContext getApplicationContext() {
            return applicationContext;
        }

        @SuppressWarnings("unchecked")
        public static <T> T getBean(String name) throws BeansException {
            return (T) applicationContext.getBean(name);
        }

}
