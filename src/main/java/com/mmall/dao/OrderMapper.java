package com.mmall.dao;

import com.mmall.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    Order selectByUserIdOrderNo(Integer userId,Long orderNo);
    Order selectByOrderNo(Long orderNo);

    Order selectByUserIdAndOrderNo(Integer userId, Long orderNo);
}