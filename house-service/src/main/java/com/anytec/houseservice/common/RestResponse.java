package com.anytec.houseservice.common;

import com.anytec.houseservice.enums.RestCode;
import lombok.Data;

@Data
public class RestResponse<T> {

    private int code;

    private String msg;

    private T result;

    public static <T>RestResponse<T> success(){
        RestResponse<T> response = new RestResponse<T>();
        return response;
    }

    public static <T>RestResponse<T> success(T result){
        RestResponse<T> response = new RestResponse<T>();
        response.setResult(result);
        return response;
    }

    public static <T>RestResponse<T> error(RestCode restCode){

        return new RestResponse<T>(restCode.getCode(),restCode.getMsg());
    }

    public RestResponse(){
         this(RestCode.OK.getCode(),RestCode.OK.getMsg());
    }

    public RestResponse (int code,String msg){
        this.code = code;
        this.msg = msg;
    }






}
