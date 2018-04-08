package com.mmall.service;


import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> delete(Integer shippingId);
     ServerResponse<String> update(Integer userId, Shipping shipping);
     ServerResponse<Shipping> select(Integer shippingId);
     ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
