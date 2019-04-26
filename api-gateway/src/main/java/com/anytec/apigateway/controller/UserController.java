package com.anytec.apigateway.controller;

import com.anytec.apigateway.common.result.ListResponse;
import com.anytec.apigateway.common.UserContext;
import com.anytec.apigateway.common.model.User;
import com.anytec.apigateway.common.result.ResultMsg;
import com.anytec.apigateway.service.AgencyService;
import com.anytec.apigateway.service.UserService;
import com.anytec.apigateway.utils.HashUtils;
import com.anytec.apigateway.utils.UserHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AgencyService agencyService;

    /**
     * 注册流程：
     * 1.注册验证 2.发送邮件 3.验证失败重定向到注册页面
     * 注册页获取：根据account对象为依据判断是否注册页获取请求
     */
    @RequestMapping("/accounts/register")
    public String accountsRegister(User account, ModelMap modelMap){
        if(account ==  null || account.getName() == null){
            return "/accounts/register";
        }
        //用户验证
        ResultMsg resultMsg = UserHelper.validate(account);
        if(resultMsg.isSuccess()){
            boolean exist = userService.isExist(account.getEmail());
            if(!exist){
                userService.addAccount(account);
                modelMap.put("email",account.getEmail());
                return "/accounts/registerSubmit";
            }  else {
                return "redirect:/accounts/register?" + resultMsg.asUrlParams();
            }

        } else {
            return "redirect:/accounts/register?"+resultMsg.asUrlParams();
        }
    }

    /**
     * 用户激活
     * @param key
     * @return
     */
    @RequestMapping("/accounts/verify")
    public String verify(String key){
        boolean result = userService.enable(key);
        if(result){
            return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
        } else {
            return "redirect:/accounts/register?"+ResultMsg.errorMsg("激活失败，请确认链接是否过期");
        }
    }

    /**
     * 登陆接口
     */
    @RequestMapping("/accounts/signin")
    public String signin(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String target = request.getParameter("target");
        //如果是登陆页的请求
        if(username == null || password == null){
            request.setAttribute("target",target);
            return "/login/signin";
        }

        User user= userService.auth(username,password);
        if(user == null){
            return "redirect:/accounts/signin?" +"target="+target+"&username="+username+"&"
                    +ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        }else {
            UserContext.setUser(user);
            return StringUtils.isNoneBlank(target) ? "redirect:"+target : "redirect:/index";
        }
    }

    /**
     * 登出接口
     * @param request
     * @return
     */
    @RequestMapping("/accounts/logout")
    public String logout(HttpServletRequest request){
        User user = UserContext.getUser();
        userService.logout(user.getToken());
        return "redirect:/index";
    }

    //------------------------------------个人信息页---------------------------------------
    @RequestMapping(value = "/accounts/profile",method = {RequestMethod.GET,RequestMethod.POST})
    public String profile(ModelMap modelMap){

        ListResponse<User> list =  agencyService.getAllAgency();
        modelMap.addAttribute("agencyList", list);
        return "/accounts/profile";
    }

    @RequestMapping(value="accounts/profileSubmit",method={RequestMethod.POST,RequestMethod.GET})
    public String profileSubmit(HttpServletRequest req,User updateUser,ModelMap  model){
        if (updateUser.getEmail() == null) {
            return "redirect:/accounts/profile?" + ResultMsg.errorMsg("邮箱不能为空").asUrlParams();
        }
        User user = userService.updateUser(updateUser);
        UserContext.setUser(user);
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }

    /**
     * 修改密码操作
     *
     * @param email
     * @param password
     * @param newPassword
     * @param confirmPassword
     * @param mode
     * @return
     */
    @RequestMapping("/accounts/changePassword")
    public String changePassword(String email, String password, String newPassword,
                                 String confirmPassword, ModelMap mode) {
        User user = userService.auth(email, password);
        if (user == null || !confirmPassword.equals(newPassword)) {
            return "redirct:/accounts/profile?" + ResultMsg.errorMsg("密码错误").asUrlParams();
        }
        User updateUser = new User();
        updateUser.setPasswd(HashUtils.encryPassword(newPassword));
        updateUser.setEmail(email);
        userService.updateUser(updateUser);
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }
}
