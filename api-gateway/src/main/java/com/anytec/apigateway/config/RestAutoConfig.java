package com.anytec.apigateway.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.apache.http.client.HttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 配置restTemplate,既支持直连访问，又支持以客户端负载均衡方式调用
 */
@Configuration
public class RestAutoConfig {

    public static class RestTemplateConfig{

        @Bean
        @LoadBalanced//spring 对restTemplate进行定制，加入loadbalance拦截器进行ip:port的替换
        RestTemplate lbRestTemplate(HttpClient httpClient){
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            restTemplate.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("utf-8")));
            restTemplate.getMessageConverters().add(1,new FastJsonHttpMessageConvert5());
            return restTemplate;

        }

        @Bean
        RestTemplate directRestTemplate(HttpClient httpClient){
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            restTemplate.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("utf-8")));
            restTemplate.getMessageConverters().add(1,new FastJsonHttpMessageConvert5());
            return restTemplate;

        }
    }

    public static class FastJsonHttpMessageConvert5 extends FastJsonHttpMessageConverter4 {

        static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

        public FastJsonHttpMessageConvert5(){
            setDefaultCharset(DEFAULT_CHARSET);
            setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,new MediaType("application","*+json")));
        }

    }
}
