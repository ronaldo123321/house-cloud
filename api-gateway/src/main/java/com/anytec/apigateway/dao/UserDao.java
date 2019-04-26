package com.anytec.apigateway.dao;

import com.anytec.apigateway.common.result.ListResponse;
import com.anytec.apigateway.common.result.RestResponse;
import com.anytec.apigateway.common.model.User;
import com.anytec.apigateway.config.GenericConfig;
import com.anytec.apigateway.utils.Rests;
import com.anytec.apigateway.utils.RestsUtil;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DefaultProperties(groupKey = "userDao",threadPoolKey = "userDao",
commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1000"),
},threadPoolProperties = {@HystrixProperty(name = "coreSize",value = "10"),
@HystrixProperty(name = "maxQueueSize",value = "1000")})
@Slf4j
public class UserDao {

    @Autowired
    private GenericConfig rest;

    @Value("${user.service.name}")
    String userService;


    @HystrixCommand
    public List<User> getUserList(User query) {
        String url = "http://" + userService +"/user/getList";
        RestResponse<List<User>> restResponse = rest.post(url, query, new ParameterizedTypeReference<RestResponse<List<User>>>() {
        }).getBody();

        if(restResponse.getCode() == 0){
            return restResponse.getResult();
        } else {
            return Lists.newArrayList();
        }

    }
    @HystrixCommand
    public User addUser(User account) {
        
        String url = "http://" + userService + "/user/add";
        RestResponse<User> restResponse = rest.post(url, account, new ParameterizedTypeReference<RestResponse<User>>() {
        }).getBody();
        if(restResponse.getCode() == 0){
            return restResponse.getResult();
        } else {
            throw new IllegalStateException("Can not add user");
        }

    }
    @HystrixCommand
    public boolean enable(String key){
        String url = "http://" + userService + "/user/enable?key=" + key;
       RestResponse<Object> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<Object>>() {
        }).getBody();
       return restResponse.getCode() == 0;
    }
    @HystrixCommand
    public User authUser(User user) {

        String url = "http://" + userService + "/user/auth";
        RestResponse<User> restResponse = rest.post(url, user, new ParameterizedTypeReference<RestResponse<User>>() {
        }).getBody();
        if(restResponse.getCode() == 0){
            return restResponse.getResult();
        }
        throw new IllegalStateException("can not  auth user");

    }
    @HystrixCommand
    public void logout(String token) {

        String url = "http://" + userService + "/user/logout?token="+token;
        rest.get(url, new ParameterizedTypeReference<RestResponse<Object>>() {});

    }

    /**
     * 服务降级处理
     * @param value
     * @return
     */
    public User getUserByTokenFb(String value){
        log.info("【getUserByToken】调用降级处理，token={}",value);
        return new User();
    }

    @HystrixCommand(fallbackMethod = "getUserByTokenFb")
    public User getUserByToken(String value) {

        String url = "http://" + userService + "/user/get?token=" + value;
        RestResponse<User> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {
        }).getBody();
        if(restResponse == null || restResponse.getCode() != 0){
            return null;
        }
        return restResponse.getResult();

    }
    @HystrixCommand
    public ListResponse<User> getAllAgency() {

          return   Rests.exc(() ->{
                String url = RestsUtil.toUrl(userService, "/agency/agentList");
                RestResponse<ListResponse<User>> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<ListResponse<User>>>() {}).getBody();
                return restResponse;
            }).getResult();


    }

    @HystrixCommand
    public User updateUser(User user){


            return Rests.exc(() ->{
                String url = RestsUtil.toUrl(userService,"user/update");
                RestResponse<User> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {
                }).getBody();
                return restResponse;
            }).getResult();

    }
    @HystrixCommand
    public User getAgentById(Long id) {

        return Rests.exc(()->{
            String url = Rests.toUrl(userService, "/agency/agencyDetail?id=" + id);
            RestResponse<User> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {
            }).getBody();
            return restResponse;
        }).getResult();

    }
}
