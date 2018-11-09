package com.curtain.wechatmini.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * @author Curtain
 * @date 2018/11/8 9:08
 */

@Configuration
@EnableJdbcHttpSession
public class HttpSessionConfig {

    @Bean
    public HttpSessionStrategy httpSessionStrategy(){
        return  new HeaderHttpSessionStrategy();
    }

}

