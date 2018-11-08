package com.curtain.security.wechatmp.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Curtain
 * @date 2018/11/8 9:27
 */

public class WeChatOAuth2ClientAuthenticationProcessingFilter extends OAuth2ClientAuthenticationProcessingFilter {

    public WeChatOAuth2ClientAuthenticationProcessingFilter(String path) {
        super(path);
        ((SavedRequestAwareAuthenticationSuccessHandler)this.getSuccessHandler()).setTargetUrlParameter("state");

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (UserApprovalRequiredException ex) {
            throw ex;
        } catch (UserRedirectRequiredException ex2) {
            throw ex2;
        } catch (Exception e) {
            e.printStackTrace();
            this.unsuccessfulAuthentication((HttpServletRequest) req, (HttpServletResponse) res, new AuthenticationException("") {
            });
        }
    }
};