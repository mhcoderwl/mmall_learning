package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JedisUtil;
import com.mmall.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mmall.pojo.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    IUserService userService;
    @RequestMapping(value= "/login.do", method = RequestMethod.POST)
    @ResponseBody//json序列化
    public ServerResponse<User> login(String username, String password, HttpSession session,HttpServletResponse httpServletResponse){
        ServerResponse<User> response= userService.login(username,password);

        if(response.isSuccess()) {
           // session.setAttribute(Const.CURRENT_USER, response.getData());//登录用户信息保存在session中
            //写入Cookie
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            JedisUtil.setex(session.getId(),JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }
    @RequestMapping(value= "/logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletResponse response, HttpServletRequest request){
        CookieUtil.delLoginToken(request,response);
        JedisUtil.del(CookieUtil.readLoginToken(request));
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
    public ServerResponse<User> getUserInfo(HttpServletRequest request){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
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
    public ServerResponse<String> resetPasswdLogin(HttpServletRequest request,String passwordOld,String passwordNew){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return userService.resetPasswdlogin(user,passwordOld,passwordNew);
    }
    //登录状态更新信息，除了用户名和密码之外的信息。
    @RequestMapping(value="/update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfo(HttpServletRequest request,User user){
        String token=CookieUtil.readLoginToken(request);
        String userStr=JedisUtil.get(token);
        User currentUser= JsonUtil.string2Obj(userStr,User.class);
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        user.setPassword(currentUser.getPassword());
        ServerResponse<User> response=userService.updateInfo(user);
        //成功后修改session中的信息
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            JedisUtil.setex(token,JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }
    @RequestMapping(value="/get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfo(HttpServletRequest request){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            //强制登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"需要登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return userService.getInfo(user.getId());
    }
}
