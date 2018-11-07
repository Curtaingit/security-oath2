package com.curtain.security.wechatmp.repository;


import com.curtain.security.wechatmp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by cx on 17-11-30.
 */
@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByOpenId(String openid);

}
