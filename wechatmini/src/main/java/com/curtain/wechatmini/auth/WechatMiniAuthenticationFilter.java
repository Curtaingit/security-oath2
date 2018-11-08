package com.curtain.wechatmini.auth;


import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;


public class WechatMiniAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";

    private String codeParameter = SPRING_SECURITY_FORM_CODE_KEY;


    private boolean postOnly = true;

    public WechatMiniAuthenticationFilter() {
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

        WechatMiniAuthenticationToken authRequest = new WechatMiniAuthenticationToken(code, false);

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
            code = (String) map.get("code");
        } catch (IOException e) {
            throw new BadCredentialsException("bad code");
        }
        return code;
    }


    protected void setDetails(HttpServletRequest request,
                              WechatMiniAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Sets the parameter name which will be used to obtain the username from the login
     * request.
     *
     * @param codeParameter the parameter name. Defaults to "username".
     */
    public void setCodeParameter(String codeParameter) {
        Assert.hasText(codeParameter, "code parameter must not be empty or null");
        this.codeParameter = codeParameter;
    }


    public final String codeParameter() {
        return codeParameter;
    }

}
