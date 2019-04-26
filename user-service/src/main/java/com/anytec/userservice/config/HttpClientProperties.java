package com.anytec.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 提供给用户进行自定义配置的
 */
@ConfigurationProperties(prefix = "spring.httpclient")
@Data
public class HttpClientProperties {

    private Integer connectTimeout = 1000;

    private Integer socketTimeOut = 10000;


    private String agent = "agent";

    private Integer maxConnPerRoute = 10;

    private Integer maxConnTotaol = 50;

}
