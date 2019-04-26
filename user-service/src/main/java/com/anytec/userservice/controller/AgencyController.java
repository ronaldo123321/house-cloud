package com.anytec.userservice.controller;

import com.anytec.userservice.common.RestResponse;
import com.anytec.userservice.model.ListResponse;
import com.anytec.userservice.model.PageParams;
import com.anytec.userservice.model.User;
import com.anytec.userservice.service.AgencyService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agency")
public class AgencyController {


    @Autowired
    private AgencyService agencyService;

    /**
     * 获取经纪人列表
     * @param limit
     * @param offset
     * @return
     */
    @RequestMapping("/agentList")
    public RestResponse<ListResponse<User>> agentList(Integer limit,Integer offset){
        PageParams pageParams = new PageParams();
        pageParams.setLimit(limit);
        pageParams.setOffset(offset);
        Pair<List<User>, Long> pair = agencyService.getAllAgent(pageParams);
        ListResponse<User> response = ListResponse.build(pair.getKey(), pair.getValue());
        return RestResponse.success(response);
    }

    /**
     * 获取经纪人详情
     * @param id
     * @return
     */
    @RequestMapping("/agencyDetail")
    public RestResponse<User> getAllAgent(Long id){

        User detail = agencyService.getAgentDetail(id);
        return RestResponse.success(detail);
    }

}
