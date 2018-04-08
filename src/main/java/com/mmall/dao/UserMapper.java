package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(User record);
    int insertSelective(User record);
    User selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(User record);
    int updateByPrimaryKey(User record);
    int checkUsername(String username);
    int checkEmail(String email);
    User selectLogin(@Param("username") String username, @Param("password") String password);
    String selectQuestion(String username);
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);
    int resetPasswd(@Param("username") String username, @Param("passwordNew") String passwordNew);
    int checkPasswd(@Param("password") String password,@Param("userID") Integer userID);
    int checkEmailByUserID(@Param("userID")Integer userID,@Param("email")String email);
}