package com.xunxiang.xunxiangwikibase.config;

import com.xunxiang.xunxiangwikibase.filter.ShiroAuthFilter;
import com.xunxiang.xunxiangwikibase.shiro.MySessionManager;
import com.xunxiang.xunxiangwikibase.shiro.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig{

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.password}")
    private String redisPassword;
    @Bean
    public ShiroFilterFactoryBean shirFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //???????????????????????????????????????
        //???????????????????????????????????????logout???????????????????????????????????????
        filterChainDefinitionMap.put("/logout", "logout");
        // ???????????????????????????
        filterChainDefinitionMap.put("/admin/**", "ShiroAuthFilter");
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        // ???????????????????????????
        filterMap.put("ShiroAuthFilter", new ShiroAuthFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * ???????????????
     * ????????????????????????????????????Shiro???SimpleAuthenticationInfo??????????????????
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//????????????:????????????MD5??????;
        hashedCredentialsMatcher.setHashIterations(1);//???????????????????????????????????????????????? md5(md5(md5("")));
        return hashedCredentialsMatcher;
    }

    /**
     * ??????????????????Realm???
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
        // ?????????session?????? ??????redis
        securityManager.setSessionManager(sessionManager());
        // ????????????????????? ??????redis
        //securityManager.setCacheManager(cacheManager());
        securityManager.setRealm(shiroRealm());

        return securityManager;
    }

    /**
     * ?????????sessionManager?????????redisSessionDAO???????????????session
     **/
    @Bean(name = "sessionManager")
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setSessionDAO(redisSessionDAO());
        return mySessionManager;
    }

    /**
     * ??????shiro redisManager
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setDatabase(0);
        redisManager.setHost(redisHost);
        redisManager.setPort(redisPort);
        redisManager.setPassword(redisPassword);
        return redisManager;
    }

//    public RedisCacheManager cacheManager(){
//        RedisCacheManager cacheManager = new RedisCacheManager();
//        cacheManager.setRedisManager(redisManager());
//        return cacheManager;
//    }

    /**
     * ??????redis RedisSessionDAO shiro sessionDao????????????
     * ??????shiro-redis??????
     */
    @Bean("redisSessionDAO")
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    //cookie??????;
    @Bean
    public SimpleCookie rememberMeCookie() {
        System.out.println("ShiroConfiguration.rememberMeCookie()");
        //???????????????cookie???????????????????????????checkbox???name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");

        //<!-- ?????????cookie????????????30??? ,?????????;-->
        simpleCookie.setMaxAge(60*30);
        return simpleCookie;
    }

    //cookie????????????;
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie());
        return manager;
    }

    /**
     * ??????shiro aop????????????.
     * ??????????????????;??????????????????????????????;
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