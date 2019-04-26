package com.anytec.apigateway.controller;

import com.anytec.apigateway.common.PageData;
import com.anytec.apigateway.common.PageParams;
import com.anytec.apigateway.common.UserContext;
import com.anytec.apigateway.common.model.Comment;
import com.anytec.apigateway.common.model.House;
import com.anytec.apigateway.common.model.User;
import com.anytec.apigateway.common.model.UserMsg;
import com.anytec.apigateway.common.result.ResultMsg;
import com.anytec.apigateway.constants.CommonConstants;
import com.anytec.apigateway.service.AgencyService;
import com.anytec.apigateway.service.CommentService;
import com.anytec.apigateway.service.HouseService;
import com.google.common.base.Objects;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private CommentService commentService;

    /**
     * 获取房产列表
     * @param pageSize
     * @param pageNum
     * @param query
     * @param modelMap
     * @return
     */
    @RequestMapping(value="house/list",method={RequestMethod.POST,RequestMethod.GET})
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap){
        PageData<House> ps = houseService.queryHouse(query, PageParams.build(pageSize, pageNum));
        List<House> rcHouses =  houseService.getHotHouse(CommonConstants.RecomSize);
        modelMap.put("recomHouses", rcHouses);
        modelMap.put("vo", query);
        modelMap.put("ps", ps);
        return "/house/listing";
    }

    /**
     * 获取房产详情
     */
    @RequestMapping(value="house/detail",method={RequestMethod.POST,RequestMethod.GET})
    public String houseDetail(long id,ModelMap modelMap){
        House house = houseService.queryOneHouse(id);
        List<Comment> comments = commentService.getHouseComments(id);
        List<House> rcHouses =  houseService.getHotHouse(CommonConstants.RecomSize);
        if (house.getUserId() != null) {
            if (!Objects.equal(0L, house.getUserId())) {
                modelMap.put("agent", agencyService.getAgentDetail(house.getUserId()));
            }
        }
        modelMap.put("house", house);
        modelMap.put("recomHouses", rcHouses);
        modelMap.put("commentList", comments);
        return "/house/detail";
    }

    /**
     * 用户留言
     * @param userMsg
     * @return
     */
    @RequestMapping(value="house/leaveMsg",method={RequestMethod.POST,RequestMethod.GET})
    public String houseMsg(UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId() + "&" + ResultMsg.successMsg("留言成功").asUrlParams();
    }

    /**
     * 房产评分
     * @param rating
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value="house/rating",method={RequestMethod.POST,RequestMethod.GET})
    public ResultMsg houseRate(Double rating,Long id){
        houseService.updateRating(id,rating);
        return ResultMsg.success();
    }


    /**
     * 添加房产
     * @param modelMap
     * @return
     */
    @RequestMapping(value="house/toAdd",method={RequestMethod.POST,RequestMethod.GET})
    public String toAdd(ModelMap modelMap){
        modelMap.put("citys", houseService.getAllCitys());
        modelMap.put("communitys", houseService.getAllCommunitys());
        return "/house/add";
    }

    /**
     * 添加房产
     * @param house
     * @return
     */
    @RequestMapping(value="house/add",method={RequestMethod.POST,RequestMethod.GET})
    public String doAdd(House house){
        User user = UserContext.getUser();
        houseService.addHouse(house,user);
        return "redirect:/house/ownlist?" +ResultMsg.successMsg("添加成功").asUrlParams();
    }

    /**
     * 获取经纪人发布的房产
     * @param house
     * @param pageParams
     * @param modelMap
     * @return
     */
    @RequestMapping(value="house/ownlist",method={RequestMethod.POST,RequestMethod.GET})
    public String ownlist(House house,PageParams pageParams,ModelMap modelMap){
        User user = UserContext.getUser();
        house.setUserId(user.getId());
        house.setBookmarked(false);
        modelMap.put("ps", houseService.queryHouse(house, pageParams)) ;
        modelMap.put("pageType", "own") ;
        return "/house/ownlist";
    }

    /**
     * 房产收藏
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value="house/bookmark",method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ResultMsg bookmark(Long id,ModelMap modelMap){
        User user = UserContext.getUser();
        houseService.bindUser2House(id, user.getId(), true);
        return ResultMsg.success();
    }

    /**
     * 取消房产收藏
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value="house/unbookmark",method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ResultMsg unbookmark(Long id,ModelMap modelMap){
        User user = UserContext.getUser();
        houseService.unbindUser2House(id, user.getId(), true);
        return ResultMsg.success();
    }



}
