package com.anytec.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;




@Component
public class GenericConfig {

    @Autowired
    private RestTemplate lbRestTemplate;

    @Autowired
    private RestTemplate directRestTemplate;

    public static final String directFlag = "direct://";

    public <T>ResponseEntity<T> post(String url, Object reqBody, ParameterizedTypeReference<T> responseType){

        RestTemplate restTemplate = getRestTemplate(url);
        url = url.replace(directFlag, "");
        return restTemplate.exchange(url, HttpMethod.POST,new HttpEntity(reqBody),responseType);
    }

    public <T>ResponseEntity<T> get(String url,  ParameterizedTypeReference<T> responseType){

        RestTemplate restTemplate = getRestTemplate(url);
        url = url.replace(directFlag, "");
        return restTemplate.exchange(url, HttpMethod.GET,HttpEntity.EMPTY,responseType);
    }


    private RestTemplate getRestTemplate(String url) {

        if(url.contains(directFlag)){
            return directRestTemplate;
        }else {
            return lbRestTemplate;
        }
    }

}
