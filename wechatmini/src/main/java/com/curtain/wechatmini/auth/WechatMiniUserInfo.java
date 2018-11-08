package com.curtain.wechatmini.auth;


import com.curtain.wechatmini.domain.User;
import com.curtain.wechatmini.service.UserService;

/**
 * Created by cx on 17-12-29.
 */

public class WechatMiniUserInfo {

    public User getUser(String openid) {
        UserService userService = (UserService) SpringUtil.getBean("userService");
        User user = userService.findByOpenid(openid);
        if (user == null) {
            User rs = new User();
            rs.setOpenid(openid);
            User save = userService.save(rs);
            return rs;
        }
        return user;
    }
}