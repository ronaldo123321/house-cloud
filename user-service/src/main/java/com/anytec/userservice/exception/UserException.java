package com.anytec.userservice.exception;

public class UserException extends RuntimeException implements WithTypeException {

    private Type type;

    public UserException(Type type,String message){
        super(message);
        this.type = type;
    }

    public Type type(){
        return type;
    }

    public enum Type{
        USER_NOT_LOGIN,USER_NOT_FOUND,USER_AUTH_FAIL
    }
}
