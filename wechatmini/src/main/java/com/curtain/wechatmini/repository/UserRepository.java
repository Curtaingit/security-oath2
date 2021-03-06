package com.curtain.wechatmini.repository;

import com.curtain.wechatmini.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Curtain
 * @date 2018/11/8 9:02
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByOpenid(String openid);

}
