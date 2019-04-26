package com.anytec.userservice.mapper;


import com.anytec.userservice.model.Agency;
import com.anytec.userservice.model.PageParams;
import com.anytec.userservice.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgencyMapper {

    List<Agency> select(Agency agency);

    int insert(Agency agency);

    List<User> selectAgent(@Param("user") User user,@Param("pageParams") PageParams pageParams);

    Long selectAgentCount(@Param("user") User user);
}
