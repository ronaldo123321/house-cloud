package com.anytec.houseservice.service;

import com.anytec.houseservice.common.LimitOffset;
import com.anytec.houseservice.dao.UserDao;
import com.anytec.houseservice.enums.HouseUserType;
import com.anytec.houseservice.mapper.HouseMapper;
import com.anytec.houseservice.model.*;
import com.anytec.houseservice.utils.BeanHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private UserDao userDao;

    @Value("${file.prefix}")
    String  imgPrefix;

    /**
     * 添加房产，绑定房产到用户关系
     * @param house
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void addHouse(House house, Long userId) {
        BeanHelper.setDefaultProp(house,House.class);
        BeanHelper.onInsert(house);
        houseMapper.insert(house);
        List<House> houses = houseMapper.selectHouse(house, LimitOffset.build(1, 0));
        House house1 = new House();
        if(!houses.isEmpty()){
             house1 = houses.get(0);
        }
        bindUser2Houser(house1.getId(),userId,HouseUserType.SALE);
    }

    /**
     * 房产关系解绑
     * @param houseId
     * @param userId
     * @param houseUserType
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindUser2Houser(Long houseId, Long userId, HouseUserType houseUserType) {

        houseMapper.delectHouseUser(houseId,userId,houseUserType.value);

    }


    @Transactional(rollbackFor = Exception.class)
    public void bindUser2Houser(Long houseId, Long userId, HouseUserType houseUserType) {

        HouseUser houseUser = houseMapper.selectHouseUser(userId, houseId, houseUserType.value);
        if(houseUser != null){
            return;
        }
        HouseUser houseUser1 = new HouseUser();
        houseUser1.setHouseId(houseId);
        houseUser1.setUserId(userId);
        houseUser1.setType(houseUserType.value);
        BeanHelper.setDefaultProp(houseUser1,HouseUser.class);
        BeanHelper.onInsert(houseUser1);
        houseMapper.insertHouseUser(houseUser1);


    }

    /**
     * 查询房产信息
     * @param query
     * @param build
     * @return
     */
    public Pair<List<House>, Long> queryHouse(House query, LimitOffset build) {

        List<House> houses = Lists.newArrayList();
        House houseQuery = query;
        if(StringUtils.isNotBlank(query.getName())){
            Community community = new Community();
            community.setName(query.getName());
            List<Community> communities = houseMapper.selectCommunity(community);
            if(!communities.isEmpty()){
                houseQuery = new House();
                houseQuery.setCommunityId(communities.get(0).getId());
            }
        }
        houses = queryAndSetImg(houseQuery,build);
        Long count = houseMapper.selectHouseCount(houseQuery);
        return ImmutablePair.of(houses,count);

    }

    /**
     * 查询单个房产信息
     * @param id
     * @return
     */
    public House queryOneHouse(Long id) {

        House query = new House();
        query.setId(id);
        List<House> houses = queryAndSetImg(query,LimitOffset.build(1,0));
        if(!houses.isEmpty()){
            return houses.get(0);
        }
        return null;

    }

    /**
     * 查询并设置图片地址
     * @param house
     * @param limitOffset
     * @return
     */
    public List<House> queryAndSetImg(House house, LimitOffset limitOffset) {
        List<House> houses = houseMapper.selectHouse(house, limitOffset);
        houses.forEach( h ->{
            h.setFirstImg( imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix+img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(p -> imgPrefix+p).collect(Collectors.toList()));

        } );
        return houses;
    }

    /**
     * 向经纪人发送留言通知
     * @param userMsg
     */
    public void addUserMsg(UserMsg userMsg) {

        BeanHelper.onInsert(userMsg);
        BeanHelper.setDefaultProp(userMsg,UserMsg.class);
        houseMapper.insertUserMsg(userMsg);
        User user = userDao.getAgentDetail(userMsg.getAgentId());
        log.info("调用邮箱服务，发送邮件到指定经纪人,userEmail={}",user.getEmail());

    }

    /**
     * 更新评分
     * @param id
     * @param rating
     */
    public void updateRating(Long id, Double rating) {

        House house = queryOneHouse(id);
        Double oldRating = house.getRating();
        Double newRating = oldRating.equals(0D) ? rating : Math.min(Math.round(oldRating + rating) / 2, 5);
        House updateHouse = new House();
        updateHouse.setId(id);
        updateHouse.setRating(newRating);
        houseMapper.updateHouse(updateHouse);

    }

    /**
     * 获取所有小区
     * @return
     */
    public List<Community> getAllCommunitys() {
        List<Community> communities = houseMapper.selectCommunity(new Community());
        return communities;
    }

    /**
     * 获取所有城市
     * @return
     */
    public List<City> getAllCitys() {
        City city = new City();
        return houseMapper.selectAllCitys(city);
    }
}
