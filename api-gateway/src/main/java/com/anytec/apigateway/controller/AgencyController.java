package com.anytec.apigateway.controller;

import com.anytec.apigateway.common.model.User;
import com.anytec.apigateway.service.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    /**
     * 获取经纪人详情
     */
    @RequestMapping("/agency/agentDetail")
    public String agentDetail(Long id, ModelMap modelMap){
        User user  = agencyService.getAgentDetail(id);
        modelMap.put("agent",user);
        return "/agent/agentDetail";

    }


}
