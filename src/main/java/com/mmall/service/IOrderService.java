package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

public interface IOrderService {
    ServerResponse pay(Long orderNo,Integer userId,String path);
    ServerResponse aliCallback(Map<String,String> params);
    ServerResponse<String> cancel(Integer userId,Long orderNo);
    ServerResponse getOrderCartProduct(Integer userId);
    ServerResponse queryOrderPayStatus(Integer id, Long orderNo);
    ServerResponse createOrder(Integer userId,Integer shippingId);
    ServerResponse<OrderVo>getOrderDetail(Integer userId, Long orderNo);
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);
}
