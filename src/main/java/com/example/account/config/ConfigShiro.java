package com.example.account.config;

import com.example.common.util.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class ConfigShiro {

        //将自己的验证方式加入容器
        @Bean
        public MyShiroRealm myShiroRealm() {
            MyShiroRealm myShiroRealm = new MyShiroRealm();
            myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
            return myShiroRealm;
        }
        /**
         * 凭证匹配器
         *
         * @return
         */
        @Bean
        public HashedCredentialsMatcher hashedCredentialsMatcher() {
            HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

            hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
            hashedCredentialsMatcher.setHashIterations(StringUtils.encryNum);
            return hashedCredentialsMatcher;
        }
        //权限管理，配置主要是Realm的管理认证
        @Bean
        public SecurityManager securityManager() {
            DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
            securityManager.setRealm(myShiroRealm());
            return securityManager;
        }

        //Filter工厂，设置对应的过滤条件和跳转条件
        @Bean
        public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
            ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
            shiroFilterFactoryBean.setSecurityManager(securityManager);
            Map<String,String> map = new HashMap<String, String>();
            //登出
            map.put("/logout","logout");
            map.put("/user/login","anon");
            map.put("/user/register","anon");
            map.put("/swagger-ui.html","anon");
            map.put("/swagger-resources/**","anon");
            //对所有用户认证
            map.put("/**","authc");
            //登录
            shiroFilterFactoryBean.setLoginUrl("/user/login");
            //首页
            shiroFilterFactoryBean.setSuccessUrl("/index");
            //错误页面，认证不通过跳转
            shiroFilterFactoryBean.setUnauthorizedUrl("/error");
            shiroFilterFactoryBean.setFilterChainDefinitionMap(map);


            return shiroFilterFactoryBean;
        }

        //加入注解的使用，不加入这个注解不生效
        @Bean
        public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
            AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
            authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
            return authorizationAttributeSourceAdvisor;
        }
}
