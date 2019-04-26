package com.anytec.apigateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.concurrent.Callable;

@Slf4j
public class RestsUtil {


    private RestsUtil(){

    }

    private static DefaultHandler defaultHandler = new DefaultHandler();

    /**
     * 执行服务调用，并返回状态
     */
    public static <T> T exe(Callable<T> callable) throws Exception {
        return  exe(callable,defaultHandler);
    }

    public static <T> T exe(Callable<T> callable,ResultHandler resultHandler) throws Exception {
        T result = sendReq(callable);
        return resultHandler.handle(result);
    }

    public static String toUrl(String serviceName,String path){
        return "http://" + serviceName + path;

    }


    public static class DefaultHandler implements ResultHandler{

        @Override
        public <T> T handle(T result) {
            int code = 1;
            String msg = "";
            try {
                code = (Integer) FieldUtils.readDeclaredField(result,"code",true);
                msg = (String) FieldUtils.readDeclaredField(result,"msg",true);

            } catch (IllegalAccessException e) {

            }
            if(code != 0){
                try {
                    throw new Exception("Get errorNo" + code + "when exe rest call with errorMsg");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    public interface ResultHandler{
        <T> T handle(T result);
    }

    public static <T> T sendReq(Callable<T> callable) throws Exception {
        T result = null;
        try {
           result = callable.call();
        } catch (Exception e) {
           throw new Exception("sendReq error");
        } finally {
            log.info("result={}",result);
        }
        return result;

    }


}
