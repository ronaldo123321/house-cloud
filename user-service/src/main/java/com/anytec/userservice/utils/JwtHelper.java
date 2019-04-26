package com.anytec.userservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtHelper {

    public static final String SECRET = "session_secret";

    public static final String ISSUER = "mooc_user";

     public static String genToken(Map<String,String> claims){
        try {
            //声明加密算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //设置jwt过期时间为一天
            JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER).withExpiresAt(DateUtils.addDays(new Date(),1));
            //将claims存储到jwt中
            claims.forEach((k,v)->builder.withClaim(k,v));
            return builder.sign(algorithm).toString();
        } catch (Exception e){
            throw new  RuntimeException(e);
        }

     }

    /**
     * 校验token
     * @param token
     * @return
     */
     public static Map<String,String> verifyToken(String token){
         Algorithm algorithm = null;
         algorithm = Algorithm.HMAC256(SECRET);
         JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
         DecodedJWT jwt = jwtVerifier.verify(token);
         Map<String, Claim> claims = jwt.getClaims();
         HashMap<String, String> resultMap = Maps.newHashMap();
         claims.forEach((k, v) -> resultMap.put(k, v.asString()));
         return resultMap;

     }
}
