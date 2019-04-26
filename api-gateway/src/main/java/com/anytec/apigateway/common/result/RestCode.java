package com.anytec.apigateway.common.result;

import lombok.Data;
import lombok.Getter;

@Getter
public enum RestCode {

    OK(0,"OK"),
    UNKNOW_ERROR(1,"服务异常"),
    WRONG_PAGE(10100,"页码不存在")
    ;

    private int code;

    private String msg;

    RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }



}
