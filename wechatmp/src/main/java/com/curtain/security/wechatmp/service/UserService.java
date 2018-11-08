package com.curtain.security.wechatmp.service;

import com.curtain.security.wechatmp.domain.User;

/**
 * @author Curtain
 * @date 2018/11/8 8:58
 */

public interface UserService {

    User findByOpenid(String openid);

    User save(User user);
}
