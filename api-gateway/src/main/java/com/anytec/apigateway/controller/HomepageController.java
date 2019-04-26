package com.anytec.apigateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class HomepageController {

  @Autowired
  private DiscoveryClient discoveryClient;

  /**
   * 项目首页
   * @param modelMap
   * @return
   */
  @RequestMapping("index")
  public String accountsRegister(ModelMap modelMap){
    return "/homepage/index";
  }

  @RequestMapping("")
  public String index(ModelMap modelMap){
    return "redirect:/index";
  }
}
