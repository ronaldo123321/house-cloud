package com.anytec.apigateway.utils;


import com.anytec.apigateway.common.model.User;
import com.anytec.apigateway.common.result.ResultMsg;
import org.apache.commons.lang3.StringUtils;


public class UserHelper {

    /**
     * 用户注册信息校验
     */
    public static ResultMsg validate(User account){

        if(StringUtils.isBlank(account.getEmail())){
            return ResultMsg.errorMsg("Email 有误");
        }

        if(StringUtils.isBlank(account.getName())){
            return ResultMsg.errorMsg("Name 有误");
        }

        if(StringUtils.isBlank(account.getConfirmPasswd()) || StringUtils.isBlank(account.getPasswd()) || !account.getPasswd().equals(account.getConfirmPasswd())){
            return ResultMsg.errorMsg("Passwd 有误");
        }

        if(account.getPasswd().length() < 6){
            return ResultMsg.errorMsg("密码需大于6位");
        }

        return ResultMsg.successMsg("");


    }
}
