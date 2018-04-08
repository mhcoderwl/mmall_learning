package com.mmall.service;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
public interface IUserService {
    public ServerResponse<User> login(String username, String password);
    public ServerResponse<String> register(User user);
    public ServerResponse<String> checkValid(String str,String type);
    public ServerResponse<String> selectQuestion(String username);
    public ServerResponse<String> checkQuestionAnswer(String username,String question,String answer);
    public ServerResponse<String> resetPasswd(String username,String passwordNew,String forgetToken);
    public ServerResponse<String> resetPasswdlogin(User user,String passwdOld,String passwdNew);
    public ServerResponse<User> updateInfo(User user);
    public ServerResponse<User> getInfo(Integer id);
    public ServerResponse checkAdminRole(User user);
}
