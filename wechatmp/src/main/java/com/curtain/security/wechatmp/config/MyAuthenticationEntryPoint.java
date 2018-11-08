package com.curtain.security.wechatmp.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过程中失败处理
 * @author Curtain
 * @date 2018/11/8 9:09
 */

@Component("myAuthenticationEntryPoint")
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );

    }
}
