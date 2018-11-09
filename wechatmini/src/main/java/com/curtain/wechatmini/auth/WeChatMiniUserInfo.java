package com.curtain.wechatmini.auth;


import com.curtain.wechatmini.domain.User;
import com.curtain.wechatmini.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理用户信息
 *
 * @author Curtain
 * @date 2018/11/8:35
 */

@Component
public class WeChatMiniUserInfo {

    @Autowired
    private UserService userService;

    public User getUser(String openid) {
        User user = userService.findByOpenid(openid);
        if (user == null) {
            User rs = new User();
            rs.setOpenid(openid);
            User save = userService.save(rs);
            return save;
        }
        return user;
    }
}