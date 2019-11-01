package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import com.github.pagehelper.PageHelper;
@Controller
@RequestMapping("/user")
public class UserController {
        @Resource
        public UserService userService;


//显示登录页面
    @RequestMapping(value = "/login.html")
    public String Login(){

        return "login";
    }

    //用户登录
    @RequestMapping(value = "/dologin.html",method = RequestMethod.POST)
    public String Login(String userCode, String userPassword, HttpSession session) {
        User user = userService.login(userCode, userPassword);

        System.out.print("ffffffffff");
        if (user != null) {
            session.setAttribute(Constants.USER_SESSION, user);
            return "redirect:/user/shou.html";
        }
        return "login";
    }

    @RequestMapping("/shou.html")
    public String frame(HttpSession session){
User user=(User)session.getAttribute(Constants.USER_SESSION);
if(user==null){
    return "login";
}
        return "shou";

    }


}



