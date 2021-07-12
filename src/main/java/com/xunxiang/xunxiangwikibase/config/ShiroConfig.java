package com.xunxiang.xunxiangwikibase.config;

import com.xunxiang.xunxiangwikibase.filter.ShiroAuthFilter;
import com.xunxiang.xunxiangwikibase.shiro.MySessionManager;
import com.xunxiang.xunxiangwikibase.shiro.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig{
    @Bean
    public ShiroFilterFactoryBean shirFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序不能颠倒
        //配置退出过滤器，修改了默认logou过滤器，清除相应的缓存信息
        filterChainDefinitionMap.put("/logout", "logout");
        // 配置需要拦截的链接
        filterChainDefinitionMap.put("/wikibook/**", "ShiroAuthFilter");
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        // 自定义的登录过滤器
        filterMap.put("ShiroAuthFilter", new ShiroAuthFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(md5("")));
        return hashedCredentialsMatcher;
    }

    /**
     * 注入自定义的Realm类
     **/
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm myShiroRealm = new ShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        securityManager.setRealm(shiroRealm());

        return securityManager;
    }

    /**
     * 自定义sessionManager，使用redisSessionDAO生成并保存session
     **/
    @Bean(name = "sessionManager")
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionDAO(redisSessionDAO());
        return mySessionManager;
    }

    /**
     * 配置shiro redisManager
     * @return
     */
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setDatabase(3);
        return redisManager;
    }

    public RedisCacheManager cacheManager(){
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager());
        return cacheManager;
    }

    /**
     * 通过redis RedisSessionDAO shiro sessionDao层的实现
     * 使用shiro-redis插件
     */
    @Bean("redisSessionDAO")
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
//
//import com.xunxiang.xunxiangwikibase.shiro.ShiroRealm;
//import org.apache.shiro.cache.ehcache.EhCacheManager;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.session.mgt.SessionManager;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.CookieRememberMeManager;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Properties;
//
//@Configuration
//public class ShiroConfig {
//
//    private static final Logger LOG = LoggerFactory.getLogger(ShiroConfig.class);
//
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/logout", "logout");
////        filterChainDefinitionMap.put("/index", "user");
////        filterChainDefinitionMap.put("/", "user");
//        filterChainDefinitionMap.put("/favicon.ico", "anon");
//        filterChainDefinitionMap.put("/", "anon");
//        filterChainDefinitionMap.put("/wikibook/**", "authc");
//        //authc表示需要验证身份才能访问，还有一些比如anon表示不需要验证身份就能访问等。
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
//    public SecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(new ShiroRealm());
//        return securityManager;
//    }
//
//    /**
//     *  开启shiro aop注解支持.
//     *  使用代理方式;所以需要开启代码支持;
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//
////    @Bean
////    public SimpleMappingExceptionResolver resolver() {
////        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
////        Properties properties = new Properties();
////        properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "/403");
////        resolver.setExceptionMappings(properties);
////        return resolver;
////    }
//
//    //cookie对象;
//    @Bean
//    public SimpleCookie rememberMeCookie() {
//        System.out.println("ShiroConfiguration.rememberMeCookie()");
//        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
//        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
//
//        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
//        simpleCookie.setMaxAge(600);
//        return simpleCookie;
//    }
//
//    //cookie管理对象;
//    @Bean
//    public CookieRememberMeManager cookieRememberMeManager() {
//        System.out.println("ShiroConfiguration.rememberMeManager()");
//        CookieRememberMeManager manager = new CookieRememberMeManager();
//        manager.setCookie(rememberMeCookie());
//        return manager;
//    }
//}
