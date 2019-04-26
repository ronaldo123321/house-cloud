package com.anytec.apigateway.interceptor;

import com.anytec.apigateway.common.UserContext;
import com.anytec.apigateway.common.model.User;
import com.anytec.apigateway.constants.CommonConstants;
import com.anytec.apigateway.dao.UserDao;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String TOKEN_COOKIE = "token";

    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<String,String[]> map = request.getParameterMap();
        map.forEach((k,v) -> request.setAttribute(k, Joiner.on(",").join(v)));
        String requestURI = request.getRequestURI();
        if(requestURI.startsWith("/static") || requestURI.startsWith("/error")){
            return true;
        }

        Cookie cookie = WebUtils.getCookie(request, TOKEN_COOKIE);
        //跨域访问新增
//        if(cookie == null && null != request.getHeader("Cookie1")){
//            String cookie1 = StringUtils.substringAfterLast(request.getHeader("Cookie1"), "=");
//            cookie = new Cookie("token",cookie1);
//        }


        if(cookie != null && StringUtils.isNotBlank(cookie.getValue())){
            User user = userDao.getUserByToken(cookie.getValue());
            request.setAttribute(CommonConstants.USER_ATTRIBUTE,user);
            UserContext.setUser(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String requestURI = request.getRequestURI();
        if(requestURI.startsWith("/static") || requestURI.startsWith("/error")){
            return;
        }
        User user = UserContext.getUser();
        if(user != null && StringUtils.isNotBlank(user.getToken())){
            String token = requestURI.startsWith("logout") ? "" : user.getToken();
            Cookie cookie = new Cookie(TOKEN_COOKIE,token);
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            response.addCookie(cookie);
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
