package cn.mklaus.framework.shiro;

import cn.mklaus.framework.ResponseProperties;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author klaus
 * @date 2019-09-11 20:30
 */
@Configuration
@EnableConfigurationProperties({ShiroConfigProperties.class, ResponseProperties.class})
public class ShiroAutoConfiguration {

    private ShiroConfigProperties shiroConfigProperties;
    private ResponseProperties responseProperties;

    @Autowired
    public ShiroAutoConfiguration(ShiroConfigProperties shiroConfigProperties, ResponseProperties responseProperties) {
        this.shiroConfigProperties = shiroConfigProperties;
        this.responseProperties = responseProperties;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(shiroConfigProperties.getLoginUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroConfigProperties.getUnauthorizedUrl());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroConfigProperties.getFilterMap());

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc", new ShiroLoginFilter(this.responseProperties));
        filterMap.put("roles", new ShiroRolesFilter(this.responseProperties));
        filterMap.put("perms", new ShiroPermissionFilter(this.responseProperties));
        shiroFilterFactoryBean.setFilters(filterMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean(SecurityManager.class)
    public SecurityManager securityManager(AuthorizingRealm realm, CookieRememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    @Bean
    @ConditionalOnMissingBean(CookieRememberMeManager.class)
    public CookieRememberMeManager getRememberMeManager() {
        return new CookieRememberMeManager();
    }

    @Bean
    @ConditionalOnMissingBean(AuthorizingRealm.class)
    public AdminRealm adminRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        AdminRealm adminRealm = new AdminRealm();
        adminRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return adminRealm;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(shiroConfigProperties.getHashAlgorithmName());
        hashedCredentialsMatcher.setHashIterations(shiroConfigProperties.getHashIterations());
        return hashedCredentialsMatcher;
    }


    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
     * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    @ConditionalOnBean(LifecycleBeanPostProcessor.class)
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
