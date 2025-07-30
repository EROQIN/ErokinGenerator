package com.erokin.generator.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信开放平台配置
 *
 * @author <a href="https://github.com/EROQIN">Erokin</a>
 *   
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "wx.open")
@Data
public class WxOpenConfig {

    private String appId;

    private String appSecret;

}