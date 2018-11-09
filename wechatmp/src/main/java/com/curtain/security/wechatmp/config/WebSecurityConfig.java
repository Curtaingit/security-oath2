package com.curtain.security.wechatmp.config;


import com.curtain.security.wechatmp.auth.WeChatOAuth2ClientAuthenticationProcessingFilter;
import com.curtain.security.wechatmp.auth.WeChatUserInfoExtractor;
import com.curtain.security.wechatmp.auth.WeChatOAuth2RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CompositeFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Security配置
 *
 * @author Curtain
 * @date 2018/11/8 9:10
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入OAuth2ClientContext   需要在启动类Application上添加 @EnableOAuth2Client
     */
    @Resource
    OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private WeChatUserInfoExtractor weChatUserInfoExtractor;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/**").authenticated();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler myFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.addAllowedHeader("x-auth-token");
        configuration.addExposedHeader("x-auth-token");
        configuration.addAllowedHeader("content-type");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    /**
     * 注入微信配置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties("wechat")
    public ClientResources weChat() {
        return new ClientResources();
    }

    /**
     * 构造微信认证
     *
     * @param client
     * @param path
     * @return
     */
    private Filter ssoFilterForWeChat(ClientResources client, String path) {

        OAuth2ClientAuthenticationProcessingFilter weChatOAuth2Filter = new WeChatOAuth2ClientAuthenticationProcessingFilter(path);

        OAuth2RestTemplate oAuth2RestTemplate = new WeChatOAuth2RestTemplate(client.getClient(),
                oauth2ClientContext, client.getResource().getTokenInfoUri());

        UserInfoTokenServices tokenServices = new UserInfoTokenServices(client.getResource().
                getUserInfoUri(), client.getClient().getClientId());
        tokenServices.setPrincipalExtractor(weChatUserInfoExtractor);
        tokenServices.setRestTemplate(oAuth2RestTemplate);

        weChatOAuth2Filter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler(true));
        weChatOAuth2Filter.setRestTemplate(oAuth2RestTemplate);
        weChatOAuth2Filter.setTokenServices(tokenServices);

        return weChatOAuth2Filter;
    }

    /**
     * 组合认证过滤链
     *
     * @return
     */
    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilterForWeChat(weChat(), "/login/wechat"));
        filter.setFilters(filters);
        return filter;
    }

    class ClientResources {
        @NestedConfigurationProperty
        private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

        @NestedConfigurationProperty
        private ResourceServerProperties resource = new ResourceServerProperties();

        public AuthorizationCodeResourceDetails getClient() {
            return client;
        }

        public ResourceServerProperties getResource() {
            return resource;
        }
    }

}
