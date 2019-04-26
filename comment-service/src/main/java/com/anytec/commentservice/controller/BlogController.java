package com.anytec.commentservice.controller;

import java.util.List;

import com.anytec.commentservice.common.RestResponse;
import com.anytec.commentservice.model.Blog;
import com.anytec.commentservice.model.BlogQueryReq;
import com.anytec.commentservice.model.ListResponse;
import com.anytec.commentservice.service.BlogService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("blog")
public class BlogController {
  
  @Autowired
  private BlogService blogService;

  /**
   * 获取博客列表
   * @param req
   * @return
   */
  @RequestMapping("list")
  public RestResponse<ListResponse<Blog>> list(@RequestBody BlogQueryReq req){
      Pair<List<Blog>,Long> pair = blogService.queryBlog(req.getBlog(),req.getLimit(),req.getOffset());
      return RestResponse.success(ListResponse.build(pair.getKey(), pair.getValue()));
  }

  /**
   * 获取单个博客信息
   * @param id
   * @return
   */
  @RequestMapping("one")
  public RestResponse<Blog> one(Integer id){
      Blog blog = blogService.queryOneBlog(id);
      return RestResponse.success(blog);
  }

}