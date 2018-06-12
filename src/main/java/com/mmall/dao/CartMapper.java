package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    List<Cart> getListByUserId(Integer userId);
    int selectNotCheckedByUserId(Integer userId);
    List<Cart> selectCheckedByUserId(Integer userId);
    Cart selectByProductIdUserId(Integer userId,Integer ProductId);
    int deleteByUserIdProductIds(Integer userId,List<String> ProductIdList);
    int checkedOrUncheckedProduct(Integer userId,Integer ProductId,Integer checked);
    int selectCartProductCount(Integer userId);
}