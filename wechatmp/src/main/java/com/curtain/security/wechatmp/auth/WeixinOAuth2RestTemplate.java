package com.curtain.security.wechatmp.auth;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class WeixinOAuth2RestTemplate extends OAuth2RestTemplate {

    public WeixinOAuth2RestTemplate(AuthorizationCodeResourceDetails resource, OAuth2ClientContext context ,String hendlerurl) {
        super(resource, context);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new MappingJackson2HttpMessageConverter() {
            @Override
            protected boolean canRead(MediaType mediaType) {
                return true;
            }
        });
        this.setMessageConverters(messageConverters);
        this.setAccessTokenProvider(new WeixinAuthorizationCodeAccessTokenProvider(messageConverters,hendlerurl));
    }

    @Override
    protected URI appendQueryParameter(URI uri, OAuth2AccessToken accessToken) {
        uri = super.appendQueryParameter(uri, accessToken);
        String url = uri.toString();
        if (url.contains("$openid$")) {
            String openid = (String) accessToken.getAdditionalInformation().get("openid");
            try {

                uri = new URI(url.replace("$openid$", openid));

            } catch (URISyntaxException e) {

                e.printStackTrace();

            }
        }
        return uri;

    }
}


