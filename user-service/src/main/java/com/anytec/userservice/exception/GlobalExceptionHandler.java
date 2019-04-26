package com.anytec.userservice.exception;

import com.anytec.userservice.common.RestCode;
import com.anytec.userservice.common.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局异常处理器
     * @param request
     * @param throwable
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = Throwable.class)
    public RestResponse<Object> handler(HttpServletRequest request,Throwable throwable){
         log.error(throwable.getMessage(),throwable);
         Object object = throwable;
         RestCode restCode = Exception2CodeRepo.getCode(throwable);
         RestResponse<Object> response = new RestResponse<Object>(restCode.getCode(), restCode.getMsg());
         return response;
    }

}
