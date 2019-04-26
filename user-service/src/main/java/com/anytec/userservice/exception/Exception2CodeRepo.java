package com.anytec.userservice.exception;

import com.anytec.userservice.common.RestCode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;


public class Exception2CodeRepo {

    public static final ImmutableMap<Object,RestCode> MAP =
            ImmutableMap.<Object,RestCode>builder()
            .put(IllegelParamsException.Type.WRONG_PAGE_NUM,RestCode.WRONG_PAGE)
            .put(IllegalStateException.class,RestCode.UNKNOW_ERROR)
             .put(UserException.Type.USER_NOT_LOGIN,RestCode.TOKEN_NOT_EXIST)
            .build();

    public static Object getType(Throwable throwable){
        try {
            return FieldUtils.readDeclaredField(throwable,"type",true);
        } catch (IllegalAccessException e) {
            return null;
        }
    }



    public static RestCode getCode(Throwable throwable){
        if(throwable == null){
            return RestCode.UNKNOW_ERROR;
        }

        Object target = throwable;
        if(throwable instanceof WithTypeException){
            Object type = getType(throwable);
            if(type != null){
                target = type;
            }
        }
        RestCode restCode = MAP.get(target);
        if(restCode != null){
            return restCode;
        }
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if(rootCause != null){
            return getCode(rootCause);
        }

        return  RestCode.UNKNOW_ERROR;
    }
}
