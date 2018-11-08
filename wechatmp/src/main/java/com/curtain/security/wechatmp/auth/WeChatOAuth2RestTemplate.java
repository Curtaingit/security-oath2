package com.curtain.security.wechatmp.auth;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取openid 拼接url
 *
 * @author Curtain
 * @date 2018/11/8 9:08
 */

public class WeChatOAuth2RestTemplate extends OAuth2RestTemplate {

    private final static String OPENID = "$OPENID$";

    public WeChatOAuth2RestTemplate(AuthorizationCodeResourceDetails resource, OAuth2ClientContext context , String handlerCodeUrl) {
        super(resource, context);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList();
        messageConverters.add(new MappingJackson2HttpMessageConverter() {
            @Override
            protected boolean canRead(MediaType mediaType) {
                return true;
            }
        });
        this.setMessageConverters(messageConverters);
        this.setAccessTokenProvider(new WeChatAuthorizationCodeAccessTokenProvider(
                messageConverters,handlerCodeUrl));
    }

    @Override
    protected URI appendQueryParameter(URI uri, OAuth2AccessToken accessToken) {
        uri = super.appendQueryParameter(uri, accessToken);
        String url = uri.toString();
        if (url.contains(OPENID)) {
            String openid = (String) accessToken.getAdditionalInformation().get("openid");
            try {
                uri = new URI(url.replace(OPENID, openid));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return uri;

    }
}


