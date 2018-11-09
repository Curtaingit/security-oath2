package com.curtain.wechatmini.auth;


import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author Curtain
 * @date 2018/11/8:39
 */

public class WeChatMiniAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";

    private boolean postOnly = true;

    public WeChatMiniAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login/wechatmini", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String code = obtainCode(request);

        if (code == null) {
            code = "";
        }

        code = code.trim();

        WeChatMiniAuthenticationToken authRequest = new WeChatMiniAuthenticationToken(code, false);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    protected String obtainCode(HttpServletRequest request) {
        String code = null;
        try {
            InputStream inputStream = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            Map map = (Map) JSON.parse(line.toString());
            code = (String) map.get(SPRING_SECURITY_FORM_CODE_KEY);
        } catch (IOException e) {
            throw new BadCredentialsException("bad code");
        }
        return code;
    }


    protected void setDetails(HttpServletRequest request,
                              WeChatMiniAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
