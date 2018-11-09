package com.curtain.wechatmini.config;



import com.curtain.wechatmini.auth.WeChatMiniAuthenticationFilter;
import com.curtain.wechatmini.auth.WeChatMiniAuthenticationProvider;
import com.curtain.wechatmini.auth.WeChatMiniUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CompositeFilter;

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

    @Autowired
    private WeChatMiniResources weChatMiniResources;

    @Autowired
    private WeChatMiniUserInfo weChatMiniUserInfo;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        AuthenticationManager am = this.authenticationManager();
        http.cors().and()
                .csrf().disable()
                .addFilterBefore(ssoFilter(am), BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/login/wechatmini").permitAll()
                .antMatchers("/**")
                .authenticated();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new WeChatMiniAuthenticationProvider(weChatMiniResources,weChatMiniUserInfo));
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

    private Filter ssoFilter(AuthenticationManager am) {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        WeChatMiniAuthenticationFilter wmaFilter = new WeChatMiniAuthenticationFilter();
        wmaFilter.setAuthenticationManager(am);
        wmaFilter.setAuthenticationSuccessHandler(this.myAuthenticationSuccessHandler);
        wmaFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
        filters.add(wmaFilter);
        filter.setFilters(filters);
        return filter;
    }

}
