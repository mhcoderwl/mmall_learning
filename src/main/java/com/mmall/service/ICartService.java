package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVO;

import java.util.List;

public interface ICartService {
    ServerResponse<CartVO> getProductList(Integer userId);
    ServerResponse<CartVO> add(Integer userId,Integer productId,Integer count);
    ServerResponse<CartVO> update(Integer userId,Integer productId,Integer count);
    ServerResponse<CartVO> deleteProduct(Integer userId, String productIds);
    ServerResponse<CartVO> selectOrUnSelect (Integer userId,Integer productId,Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
