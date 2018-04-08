package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("shippingService")
public class ShippingServiceImpl implements IShippingService{
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);//为了快速查找，多一个字段，无需连表
        int count=shippingMapper.insert(shipping);
        if(count>0){
            Map map=new HashMap();
            map.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",map);
        }else
            return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> delete(Integer shippingId) {
            int count = shippingMapper.deleteByPrimaryKey(shippingId);
            if(count>0) {
                return ServerResponse.createBySuccess("删除成功");
            }else
                return ServerResponse.createBySuccess("删除失败");
    }
    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);//为了快速查找，多一个字段，无需连表
        int count=shippingMapper.updateByShipping(shipping);
        if(count>0){
            return ServerResponse.createBySuccess("新建地址成功");
        }else
            return ServerResponse.createByErrorMessage("更新地址失败");
    }
    public ServerResponse<Shipping> select(Integer shippingId){
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("更新地址成功",shipping);
    }


    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
