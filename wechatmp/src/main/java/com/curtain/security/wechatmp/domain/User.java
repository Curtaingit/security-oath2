package com.curtain.security.wechatmp.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity

@Table(name = "T_User")
@Getter@Setter
public class User {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    private Long id;

    public Long getId() {
        return id;
    }


    private String name;

    private String openId;

    private String sex;

    private String country;

    private String province;

    private String city;

    private String headImgUrl;

    private String phone;
}