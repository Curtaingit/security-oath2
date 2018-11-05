package com.curtain.securityoath2.connect;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;


import java.io.IOException;
import java.util.Map;

/**
 * 处理微信返回的令牌信息
 *
 * @author Curtain
 * @date 2018/11/5 14:14
 */
public class WeChatOAuth2Template extends OAuth2Template {

    private String clientId;

    private String clientSecret;

    private String accessTokenUrl;

    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    Logger logger = LoggerFactory.getLogger(WeChatOAuth2Template.class);

    public WeChatOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
    }

    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> additionalParameters) {

        StringBuilder accessTokenRequestUrl = new StringBuilder(accessTokenUrl);

        accessTokenRequestUrl.append("?appid=" + clientId);
        accessTokenRequestUrl.append("&secret=" + clientSecret);
        accessTokenRequestUrl.append("&code=" + authorizationCode);
        accessTokenRequestUrl.append("&grant_type=authorization_code");
        accessTokenRequestUrl.append("&redirect_uri=" + redirectUri);

        return getAccessToken(accessTokenRequestUrl);
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        StringBuilder refreshTokenUrl = new StringBuilder(REFRESH_TOKEN_URL);

        refreshTokenUrl.append("?appid=" + clientId);
        refreshTokenUrl.append("&grant_type=refresh_token");
        refreshTokenUrl.append("&refresh_token=" + refreshToken);

        return getAccessToken(refreshTokenUrl);
    }

    private AccessGrant getAccessToken(StringBuilder accessTokenRequestUrl) {
        logger.info("获取access_token, 请求URL: " + accessTokenRequestUrl.toString());

        String response = getRestTemplate().getForObject(accessTokenRequestUrl.toString(), String.class);

        logger.info("获取access_token, 响应内容: " + response);

        Map<String, Object> result = null;
        try {
            result = new ObjectMapper().readValue(response,Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回错误码时直接返回空
//        if (StringUtils.isNotBlank(MapUtils.getString(result)))

        return null;
    }
}
