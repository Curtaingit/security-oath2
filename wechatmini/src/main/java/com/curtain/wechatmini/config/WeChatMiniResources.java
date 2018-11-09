package com.curtain.wechatmini.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 小程序配置类
 *
 * @author Curtain
 * @date 2018/11/9 8:37
 */

@Component
@Data
@ConfigurationProperties("wechatmini")
public class WeChatMiniResources {

    private String appId;

    private String secret;

    private String authUrl;
}
