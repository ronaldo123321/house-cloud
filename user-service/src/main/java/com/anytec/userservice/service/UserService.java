package com.anytec.userservice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anytec.userservice.exception.UserException;
import com.anytec.userservice.mapper.UserMapper;
import com.anytec.userservice.model.User;
import com.anytec.userservice.utils.BeanHelper;
import com.anytec.userservice.utils.HashUtils;
import com.anytec.userservice.utils.JwtHelper;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Value("${file.prefix}")
    String filePrefix;

    /**
     * 通过缓存获取，设置缓存时间5min
     * 若不存在，则通过数据库获取用户对象
     * 将用户对象写入缓存
     * 返回
     * @param id
     * @return
     */
    public User getUserById(Long id){

        String key = "user:" + id;
        String json = redisTemplate.opsForValue().get(key);
        User user;
        if(Strings.isNullOrEmpty(json)){
             user = userMapper.selectById(id);
             user.setAvatar(filePrefix + user.getAvatar());
             String string = JSONObject.toJSONString(user);
             redisTemplate.opsForValue().set(key,string);
             redisTemplate.expire(key,5, TimeUnit.MINUTES);
        } else {
            user = JSON.parseObject(json, User.class);
        }
        return user;

    }


    public List<User> getUserList(User user){
        List<User> userList = userMapper.select(user);
        userList.forEach(u ->{
            u.setAvatar(filePrefix + u.getAvatar());
        });
        return userList;
    }

    /**
     * 注册用户
     * @param user
     * @param enableUrl
     */
    public void addAccount(User user,String enableUrl){
        user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
        BeanHelper.onInsert(user);
        userMapper.insert(user);
        registerNofity(user.getEmail(),enableUrl);

    }

    private void registerNofity(String email, String enableUrl) {
          String randomKey =  HashUtils.hashString(email) +  RandomStringUtils.randomAlphabetic(10);
          redisTemplate.opsForValue().set(randomKey,email);
          redisTemplate.expire(randomKey,1,TimeUnit.HOURS);
          String content = enableUrl + "?key="+randomKey;
          mailService.sendMail("房产平台用户激活",content,email);

    }

    /**
     * 注册用户激活
     * @param key
     * @return
     */
    public boolean enable(String key){
        String email = redisTemplate.opsForValue().get(key);
        if(StringUtils.isBlank(email)){
            throw new UserException(UserException.Type.USER_NOT_FOUND,"无效key");
        }
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        userMapper.update(updateUser);
        //激活完成删除Key
        redisTemplate.opsForValue().getOperations().delete(key);
        return true;
    }

    /**
     * 校验用户登陆用户名和密码  并生成token
     * @param email
     * @param passwd
     * @return
     */
    public User auth(String email, String passwd) {

        if(StringUtils.isBlank(email) || StringUtils.isBlank(passwd)){
            throw new UserException(UserException.Type.USER_AUTH_FAIL,"User Auth Fail");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswd(HashUtils.encryPassword(passwd));
        user.setEnable(1);
        List<User> userList = getUserList(user);
        if(!userList.isEmpty()){
            User retUser = userList.get(0);
            onLogin(retUser);
            return retUser;
        }
        throw new UserException(UserException.Type.USER_AUTH_FAIL,"User Auth Fail");

    }

    private void onLogin(User retUser) {
        String token =  JwtHelper.genToken(ImmutableMap.of("email",retUser.getEmail(),"name",retUser.getName(),"ts", Instant.now().getEpochSecond()+""));
        renewToken(token,retUser.getEmail());
        retUser.setToken(token);
    }


    private String renewToken(String token,String email){
        redisTemplate.opsForValue().set(email,token);
        redisTemplate.expire(email,30,TimeUnit.MINUTES);
        return token;
    }

    /**
     * 用户登陆鉴权
     * @param token
     * @return
     */
    public User getLoginedUserByToken(String token) {

        Map<String,String> map = null;
        try {
            map = JwtHelper.verifyToken(token);
        }catch (Exception e){
            throw new UserException(UserException.Type.USER_NOT_LOGIN,"User not login");
        }
        String email = map.get("email");
        Long expire = redisTemplate.getExpire(email);
        if(expire > 0){
            renewToken(token,email);
            User user = getUserByEmail(email);
            user.setToken(token);
            return user;
        }

        throw new UserException(UserException.Type.USER_NOT_LOGIN,"User not login");

    }

    /**
     * 通过邮箱获取用户信息
     * @param email
     * @return
     */
    private User getUserByEmail(String email) {

        User user = new User();
        user.setEmail(email);
        List<User> userList = getUserList(user);
        if(!userList.isEmpty()){
            return userList.get(0);
        }
        throw new UserException(UserException.Type.USER_NOT_FOUND,"User not found for"+email);

    }

    /**
     * 用户登出
     */
    public void invalidate(String token) {

        Map<String, String> map = JwtHelper.verifyToken(token);
        redisTemplate.delete(map.get("email"));
    }
}
