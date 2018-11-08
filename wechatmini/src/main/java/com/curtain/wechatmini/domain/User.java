package com.curtain.wechatmini.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 用户信息实体类
 *
 * @author Curtain
 * @date 2018/11/8 8:59
 */

@Entity
@Table(name = "T_User")
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailSeq")
    @SequenceGenerator(initialValue = 1, name = "emailSeq", sequenceName = "EMAIL_SEQUENCE")
    private Long id;

    private String name;

    private String openid;

    private String sex;

    private String country;

    private String province;

    private String city;

    private String headImgUrl;

    private String phone;
}