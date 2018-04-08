package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.service.IUserService;
import com.mmall.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mmall.pojo.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    IUserService userService;
    @RequestMapping(value= "/login.do", method = RequestMethod.POST)
    @ResponseBody//json序列化
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response= userService.login(username,password);

        if(response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());//登录用户信息保存在session中
        }
        return response;
    }
    @RequestMapping(value= "/logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
    @RequestMapping(value="/register.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<String> register(User user){
        return userService.register(user);
    }
    @RequestMapping(value="/check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    ServerResponse<String> checkValid(String str,String type){
        return userService.checkValid(str,type);
    }
    @RequestMapping(value="/get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return ServerResponse.createBySuccess(user);
    }
    @RequestMapping(value="/forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPasswd(String username){
        return userService.selectQuestion(username);
    }
    @RequestMapping(value="/forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkQuestionAnswer(String username,String question,String answer){
        return userService.checkQuestionAnswer(username,question,answer);
    }
    @RequestMapping(value="/forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPasswd(String username,String passwordNew,String forgetToken){
        return userService.resetPasswd(username,passwordNew,forgetToken);
    }
    @RequestMapping(value="/reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPasswdLogin(HttpSession session,String passwordOld,String passwordNew){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return userService.resetPasswdlogin(user,passwordOld,passwordNew);
    }
    //登录状态更新信息，除了用户名和密码之外的信息。
    @RequestMapping(value="/update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfo(HttpSession session,User user){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        user.setPassword(currentUser.getPassword());
        ServerResponse<User> response=userService.updateInfo(user);
        //成功后修改session中的信息
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    @RequestMapping(value="/get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfo(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            //强制登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"需要登录");
        }
        return userService.getInfo(user.getId());
    }
}
