package com.curtain.security.wechatmp.service;

import com.curtain.security.wechatmp.domain.User;

import java.util.List;

/**
 * Created by cx on 17-11-30.
 */

public interface UserService {

    User findByOpenid(String openid);

    User save(User user);
}
