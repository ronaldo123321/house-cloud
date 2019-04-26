package com.anytec.userservice.exception;

public class IllegelParamsException extends RuntimeException implements WithTypeException{

    private Type type;

    public IllegelParamsException(Type type,String msg){
        super(msg);
        this.type = type;
    }

    public Type getType(){
        return type;
    }

    public enum Type{
        WRONG_PAGE_NUM,WRONG_TYPE
    }
}
