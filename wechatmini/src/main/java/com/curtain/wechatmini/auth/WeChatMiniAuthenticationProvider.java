package com.curtain.wechatmini.auth;


import com.alibaba.fastjson.JSON;
import com.curtain.wechatmini.config.WeChatMiniResources;
import com.curtain.wechatmini.domain.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Curtain
 * @date 2018/11/8:50
 */

public class WeChatMiniAuthenticationProvider implements AuthenticationProvider {

    private WeChatMiniResources weChatMiniResources;
    private WeChatMiniUserInfo weChatMiniUserInfo;

    public WeChatMiniAuthenticationProvider(WeChatMiniResources weChatMiniResources, WeChatMiniUserInfo weChatMiniUserInfo) {
        this.weChatMiniResources = weChatMiniResources;
        this.weChatMiniUserInfo = weChatMiniUserInfo;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WeChatMiniAuthenticationToken authenticationToken = (WeChatMiniAuthenticationToken) authentication;
        String code = (String) authenticationToken.getPrincipal();

        String authUrl = weChatMiniResources.getAuthUrl() + "appId=" + weChatMiniResources.getAppId() +
                "&secret=" + weChatMiniResources.getSecret() + "&js_code=" + code + "&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(authUrl, String.class);
        Map map = (Map) JSON.parse(jsonData);
        String openid = (String) map.get("openid");
        if (openid == null) {
            throw new BadCredentialsException("Unable to obtain open information");
        }

        User user = weChatMiniUserInfo.getUser(openid);

        WeChatMiniAuthenticationToken authenticationResult = new WeChatMiniAuthenticationToken(user, true);
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeChatMiniAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
