package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.service.IUserService;
import com.mmall.pojo.User;
import com.mmall.util.JedisUtil;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.mmall.common.Const;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements IUserService{
    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int hasUser=userMapper.checkUsername(username);
        if(hasUser==0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // TODO: 2018/3/29 0029  //密码转MD5
        String MD5passwd=MD5Util.MD5EncodeUtf8(password);
        User user=userMapper.selectLogin(username,MD5passwd);
        if(user==null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> response=checkValid(user.getUsername(),Const.USERNAME);
        if(!response.isSuccess()){
            return response;
        }
        response=checkValid(user.getEmail(),Const.EMAIL);
        if(!response.isSuccess()){
            return response;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int state=userMapper.insert(user);
        if(state==0)
            return ServerResponse.createByErrorMessage("注册失败");
        return ServerResponse.createBySuccess("注册成功");
    }
    //注册时检查字段是否正确
    public ServerResponse<String> checkValid(String str,String type){
        if(StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int hasUser = userMapper.checkUsername(str);
                if (hasUser > 0) {
                    return ServerResponse.createByErrorMessage("用户已存在");
                }
            } else if (Const.EMAIL.equals(type)) {
                int hasEmail = userMapper.checkEmail(str);
                if (hasEmail > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已注册");
                }
            }
        }else return ServerResponse.createByErrorMessage("参数错误");
        return ServerResponse.createBySuccess("校验成功");
    }
    @Override
    public ServerResponse<String> selectQuestion(String username){
        ServerResponse<String> response=checkValid(username,Const.USERNAME);
        if(response.isSuccess()){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question=userMapper.selectQuestion(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(username);
        }
        return ServerResponse.createByErrorMessage("该用户未设置找回密码问题");
    }


    /**
     如果正确需要缓存一个token，用于重置密码时校验
     */
    @Override
    public ServerResponse<String> checkQuestionAnswer(String username, String question, String answer) {
        int count=userMapper.checkAnswer(username,question,answer);
        if(count>0){
            String forgetToken=UUID.randomUUID().toString();
            //设置缓存，迁移到redis
            //TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            JedisUtil.setex(Const.TOKEN_PREFIX+username,forgetToken,60*60*12);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    /*
    重置密码需要先判断传来的token是否失效或者正确，然后再进行业务设置。
     */
    @Override
    public ServerResponse<String> resetPasswd(String username, String passwordNew,String forgetToken) {
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，需要token");
        }
        ServerResponse response=checkValid(username,Const.USERNAME);
        if(response.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token= JedisUtil.get(Const.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或已过期");
        }
        if(StringUtils.equals(forgetToken,token)) {
            //MD5加密
            String MD5password = MD5Util.MD5EncodeUtf8(passwordNew);
            int state=userMapper.resetPasswd(username, MD5password);
            if(state>0)
                return ServerResponse.createBySuccess("密码重置成功");
        }else{
            return ServerResponse.createByErrorMessage("token错误");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPasswdlogin(User user,String passwdOld,String passwdNew) {
        //防止横向越权，需要检查旧用户密码
        int count=userMapper.checkPasswd(MD5Util.MD5EncodeUtf8(passwdOld),user.getId());
        if(count==0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwdNew));
        count=userMapper.updateByPrimaryKeySelective(user);
        if(count>0){
            return ServerResponse.createBySuccess("密码重置成功");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<User> updateInfo(User user) {
        int count=userMapper.checkEmailByUserID(user.getId(),user.getEmail());
        if(count>0){
            return ServerResponse.createByErrorMessage("邮箱已注册");
        }
        count=userMapper.updateByPrimaryKeySelective(user);
        if(count==0){
            return ServerResponse.createByErrorMessage("修改失败");
        }
        return ServerResponse.createBySuccess("修改成功",user);
    }

    @Override
    public ServerResponse<User> getInfo(Integer id) {
        User user=userMapper.selectByPrimaryKey(id);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //消去密码信息
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if(Const.Role.ROLE_ADMIN==user.getRole()){
            return ServerResponse.createBySuccess();
        }else
            return ServerResponse.createByError();
    }
}
