package com.anytec.houseservice.mapper;


import com.anytec.houseservice.common.LimitOffset;
import com.anytec.houseservice.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {


    int insert(House house);

    List<House> selectHouse(@Param("house") House query, @Param("pageParams")LimitOffset limitOffset);

    Long selectHouseCount(@Param("house") House query);

    List<Community> selectCommunity(Community community);

    int insertUserMsg(UserMsg userMsg);

    int updateHouse(House house);

    HouseUser selectHouseUser(@Param("userId") long userId,@Param("houseId") long houseId,@Param("type") long type);

    HouseUser selectHouseUserById(@Param("id") long id,@Param("type") long type);

    int insertHouseUser(HouseUser houseUser);

    int delect(Long id);

    int delectHouseUser(@Param("id") Long id,@Param("userId") long userId,@Param("type") long type);

    List<City> selectAllCitys(City city);
}
