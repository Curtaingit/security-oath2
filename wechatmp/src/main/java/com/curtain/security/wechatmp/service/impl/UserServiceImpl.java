package com.curtain.security.wechatmp.service.impl;

import com.curtain.security.wechatmp.domain.User;
import com.curtain.security.wechatmp.repository.UserRepository;
import com.curtain.security.wechatmp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Curtain
 * @date 2018/11/7 17:05
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User findByOpenid(String openid) {

        return repository.findByOpenid(openid);
    }
}
