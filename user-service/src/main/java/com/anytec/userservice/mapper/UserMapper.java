package com.anytec.userservice.mapper;


import com.anytec.userservice.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper//mybatis起步依赖扫描到该类，并注册成bean对象
public interface UserMapper {

    User selectById(Long id);

    List<User> select(User user);

    int update(User user);

    int insert(User account);

    int delete(String email);
}
