package com.anytec.userservice.controller;


import com.anytec.userservice.common.RestResponse;

import com.anytec.userservice.model.User;
import com.anytec.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    //-------------------------------查询-----------------------------------
    @RequestMapping("getById")
    public RestResponse<User> getUserById(Long id){
       return RestResponse.success( userService.getUserById(id)) ;
    }

    @RequestMapping("getList")
    public RestResponse<List<User>> getUserList(@RequestBody User user){
         return RestResponse.success(userService.getUserList(user));
    }


    //---------------------------------注册-------------------------------------
    @RequestMapping("add")
    public RestResponse<User> add(@RequestBody User user){
        userService.addAccount(user,user.getEnableUrl());
        return RestResponse.success();
    }

    @RequestMapping("enable")
    public RestResponse<User> enable(String key){
        userService.enable(key);
        return RestResponse.success();
    }

    //------------------------------登陆/鉴权-----------------------------------
    @RequestMapping("auth")
    public RestResponse<User> auth(@RequestBody User user){
        User finalUser = userService.auth(user.getEmail(), user.getPasswd());
        return RestResponse.success(finalUser);
    }

    @RequestMapping("get")
    public RestResponse<User> getUser(String token){

        User finalUser =  userService.getLoginedUserByToken(token);
        return RestResponse.success(finalUser);
    }

    @RequestMapping("logout")
    public RestResponse<User> logout(String token){
        userService.invalidate(token);
        return RestResponse.success();
    }

}
