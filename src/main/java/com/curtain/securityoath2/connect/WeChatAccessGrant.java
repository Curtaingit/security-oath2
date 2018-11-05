package com.curtain.securityoath2.connect;

import org.springframework.social.oauth2.AccessGrant;

/**
 * 处理微信返回的access_token (添加openid)
 *
 * @author Curtain
 * @date 2018/11/5 14:12
 */
public class WeChatAccessGrant extends AccessGrant {

    private String openid;

    public WeChatAccessGrant(){
        super("");
    }

    public WeChatAccessGrant(String accessToken,String scope,String refreshToken,Long expireIn){
        super(accessToken,scope,refreshToken,expireIn);
    }
}
