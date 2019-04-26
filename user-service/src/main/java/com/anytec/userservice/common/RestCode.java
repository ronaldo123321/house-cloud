package com.anytec.userservice.common;

import lombok.Getter;

@Getter
public enum RestCode {

    OK(0,"OK"),
    UNKNOW_ERROR(1,"服务异常"),
    TOKEN_NOT_EXIST(2,"token失效"),
    USER_NOT_LOGIN(3,"用户不存在"),
    WRONG_PAGE(10100,"页码不存在")
    ;

    private int code;

    private String msg;

    RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }



}
