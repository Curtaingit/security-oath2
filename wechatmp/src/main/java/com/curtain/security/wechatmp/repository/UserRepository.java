package com.curtain.security.wechatmp.repository;

import com.curtain.security.wechatmp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Curtain
 * @date 2018/11/8 9:02
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByOpenId(String openid);

}
