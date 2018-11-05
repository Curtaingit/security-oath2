package com.curtain.securityoath2.api.impl;

import com.curtain.securityoath2.api.WeChat;
import com.curtain.securityoath2.api.WeChatUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 实现返回用户信息接口
 *
 * @author Curtain
 * @date 2018/11/5 14:05
 */
public class WeChatImpl extends AbstractOAuth2ApiBinding implements WeChat {

    /**
     * 获取用户信息的url
     */
    private static final String WECHAT_URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeChatImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 获取用户信息
     *
     * @param openid
     * @return
     */
    @Override
    public WeChatUserInfo getUserInfo(String openid) {
        String url = WECHAT_URL_GET_USER_INFO + openid;

        String result = getRestTemplate().getForObject(url, String.class);

        if (StringUtils.contains(result,"errcode")) {
            return null;
        }

        WeChatUserInfo userInfo = null;

        try {
            userInfo = objectMapper.readValue(result,WeChatUserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    /**
     * 使用utf-8 替换默认的ISO-8859-1编码
     * @return
     */
    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }
}
