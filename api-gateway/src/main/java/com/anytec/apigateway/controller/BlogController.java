package com.anytec.apigateway.controller;

import java.util.List;

import com.anytec.apigateway.common.PageData;
import com.anytec.apigateway.common.PageParams;
import com.anytec.apigateway.common.model.Blog;
import com.anytec.apigateway.common.model.Comment;
import com.anytec.apigateway.common.model.House;
import com.anytec.apigateway.constants.CommonConstants;
import com.anytec.apigateway.service.CommentService;
import com.anytec.apigateway.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class BlogController {
  
  
  @Autowired
  private CommentService commentService;
  
  @Autowired
  private HouseService houseService;

  /**
   * 获取博客列表，支持分页
   * @param pageSize
   * @param pageNum
   * @param query
   * @param modelMap
   * @return
   */
  @RequestMapping(value="blog/list",method={RequestMethod.POST,RequestMethod.GET})
  public String list(Integer pageSize, Integer pageNum, Blog query, ModelMap modelMap){
      PageData<Blog> ps = commentService.queryBlogs(query, PageParams.build(pageSize, pageNum));
      List<House> houses =  houseService.getHotHouse(CommonConstants.RecomSize);
      modelMap.put("recomHouses", houses);
      modelMap.put("ps", ps);
      return "/blog/listing";
  }

  /**
   * 获取博客详情
   * @param id
   * @param modelMap
   * @return
   */
  @RequestMapping(value="blog/detail",method={RequestMethod.POST,RequestMethod.GET})
  public String blogDetail(int id,ModelMap modelMap){
      Blog blog = commentService.queryOneBlog(id);
      List<Comment> comments = commentService.getBlogComments(id);
      List<House> houses =  houseService.getHotHouse(CommonConstants.RecomSize);
      modelMap.put("recomHouses", houses);
      modelMap.put("blog", blog);
      modelMap.put("commentList", comments);
      return "/blog/detail";
  }
}
