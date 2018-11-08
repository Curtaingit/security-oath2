package com.curtain.security.wechatmp.auth;


import com.curtain.security.wechatmp.domain.User;
import com.curtain.security.wechatmp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 认证成功后  用户信息处理
 *
 * @author Curtain
 * @date 2018/11/8 9:08
 */

@Component
public class WeChatUserInfoExtractor implements PrincipalExtractor {

    @Autowired
    private UserService userService;

    @Override
    public Object extractPrincipal(Map<String, Object> map) {

        String openid = (String)map.get("openid");
        if (userService.findByOpenid(openid)==null){
            User user =new User();
            user.setName((String)map.get("nickname"));
            user.setOpenId(openid);
            user.setSex((map.get("sex").toString()));
            user.setCity((String)(map.get("city")));
            user.setProvince((String)(map.get("province")));
            user.setCountry((String)(map.get("country")));
            user.setHeadImgUrl((String)map.get("headimgurl"));
            userService.save(user);
        }

        return openid;
    }
}
