package com.anytec.userservice.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

@Configuration
@ConditionalOnClass({HttpClient.class})
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfiguration {

    @Autowired
    private LogbookHttpRequestInterceptor logbookHttpRequestInterceptor;

    @Autowired
    private LogbookHttpResponseInterceptor logbookHttpResponseInterceptor;

    private final HttpClientProperties properties;

    public HttpClientAutoConfiguration(HttpClientProperties properties) {
        this.properties = properties;
    }



    /**
     * httpclient bean定义
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient(){

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(properties.getConnectTimeout())
                .setSocketTimeout(properties.getSocketTimeOut()).build();
        HttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).
                setUserAgent(properties.getAgent())
                .setMaxConnPerRoute(properties.getMaxConnPerRoute())
                .setMaxConnTotal(properties.getMaxConnTotaol())
                .addInterceptorFirst(logbookHttpRequestInterceptor)
                .addInterceptorFirst(logbookHttpResponseInterceptor)
                .build();
        return httpclient;

    }
}
