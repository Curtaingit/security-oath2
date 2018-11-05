package com.curtain.securityoath2.api;

/**
 * 定义返回用户信息接口
 *
 * @author Curtain
 * @date 2018/11/5 14:05
 */
public interface WeChat {

    /**
     * 获取用户信息
     * @param openid
     * @return
     */
    WeChatUserInfo getUserInfo(String openid);

}
