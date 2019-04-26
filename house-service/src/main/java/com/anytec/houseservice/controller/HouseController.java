package com.anytec.houseservice.controller;

import com.anytec.houseservice.common.LimitOffset;
import com.anytec.houseservice.enums.RestCode;
import com.anytec.houseservice.common.RestResponse;
import com.anytec.houseservice.constants.CommonConstants;
import com.anytec.houseservice.enums.HouseUserType;
import com.anytec.houseservice.model.*;
import com.anytec.houseservice.service.HouseService;
import com.anytec.houseservice.service.RecommendService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("house")
public class HouseController {


    @Autowired
    private HouseService houseService;

    @Autowired
    private RecommendService recommendService;

    /**
     *房产新增
     *
     */
    @RequestMapping("add")
    public RestResponse<Object> doAdd(@RequestBody House house){
        house.setState(CommonConstants.HOUSE_STATE_UP);
        if(house.getUserId() == null){
            return RestResponse.error(RestCode.ILLEGAL_PARAMS);
        }
        houseService.addHouse(house,house.getUserId());
        return RestResponse.success();
    }

    /**
     * 房产与人员关系绑定/解绑
     * @param houseUser
     * @return
     */
    @RequestMapping("bind")
    public RestResponse<Object> delsale(@RequestBody HouseUserReq houseUser){

        Integer bindType = houseUser.getBindType();
        HouseUserType houseUserType = Objects.equals(bindType, 1) ? HouseUserType.SALE : HouseUserType.BOOKMARK;
        //如果解绑人员-房产关系
        if(houseUser.isUnBind()){
            houseService.unbindUser2Houser(houseUser.getHouseId(),houseUser.getUserId(),houseUserType);
        } else {
            houseService.bindUser2Houser(houseUser.getHouseId(),houseUser.getUserId(),houseUserType);
        }
        return RestResponse.success();

    }

    /**
     * 查询房产列表
     * @param req
     * @return
     */
    @RequestMapping("list")
    public RestResponse<ListResponse<House>> houseList(@RequestBody HouseQueryReq req){
        Integer limit = req.getLimit();
        Integer offset = req.getOffset();
        House query = req.getQuery();
        Pair<List<House>, Long> pair = houseService.queryHouse(query, LimitOffset.build(limit, offset));
        return RestResponse.success(ListResponse.build(pair.getKey(),pair.getValue()));
    }

    /**
     * 查询房产详情
     * @param id
     * @return
     */
    @RequestMapping("detail")
    public RestResponse<House> houseDetail(Long id){
        House house = houseService.queryOneHouse(id);
        recommendService.increaseHot(id);
        return RestResponse.success(house);
    }

    /**
     * 向经纪人发送留言通知
     * @param userMsg
     * @return
     */
    @RequestMapping("addUserMsg")
    public RestResponse<Object> houseMsg(@RequestBody UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return RestResponse.success();
    }

    /**
     * 更新评分
     * @param rating
     * @param id
     * @return
     */
    @RequestMapping("rating")
    public RestResponse<Object> houseRate(Double rating,Long id){
        houseService.updateRating(id,rating);
        return RestResponse.success();
    }

    /**
     * 获取热门房产
     * @param size
     * @return
     */
    @RequestMapping("hot")
    public RestResponse<List<House>> getHotHouse(Integer size){
        List<House> list = recommendService.getHotHouse(size);
        return RestResponse.success(list);
    }

    /**
     * 获取最新房产
     * @return
     */
    @RequestMapping("lastest")
    public RestResponse<List<House>> getLastest(){
        return RestResponse.success(recommendService.getLastest());
    }

    /**
     * 获取所有小区
     * @return
     */
    @RequestMapping("allCommunitys")
    public RestResponse<List<Community>> toAdd(){
        List<Community> list = houseService.getAllCommunitys();
        return RestResponse.success(list);
    }

    /**
     * 获取所有城市
     * @return
     */
    @RequestMapping("allCitys")
    public RestResponse<List<City>> allCitys(){
        List<City> list = houseService.getAllCitys();
        return RestResponse.success(list);
    }


}
