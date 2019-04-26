package com.anytec.apigateway.controller;

import com.anytec.apigateway.common.UserContext;
import com.anytec.apigateway.common.model.User;
import com.anytec.apigateway.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class CommentController {
  
  @Autowired
  private CommentService commentService;

  /**
   * 用户评论
   * @param content
   * @param houseId
   * @param modelMap
   * @return
   */
  @RequestMapping(value="comment/leaveComment")
  public String leaveComment(String content,Long houseId,ModelMap modelMap){
      User user = UserContext.getUser();
      Long userId =  user.getId();
      commentService.addHouseComment(houseId,content,userId);
      return "redirect:/house/detail?id=" + houseId;
  }
  
  @RequestMapping(value="comment/leaveBlogComment")
  public String leaveBlogComment(String content,Integer blogId,ModelMap modelMap){
      User user = UserContext.getUser();
      Long userId =  user.getId();
      commentService.addBlogComment(blogId,content,userId);
      return "redirect:/blog/detail?id=" + blogId;
  }

}
