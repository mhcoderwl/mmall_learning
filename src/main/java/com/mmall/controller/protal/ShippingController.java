package com.mmall.controller.protal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JedisUtil;
import com.mmall.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    private IShippingService shippingService;
    @RequestMapping("add.do")
    @ResponseBody
    ServerResponse add(HttpServletRequest request, Shipping shipping){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  shippingService.add(user.getId(),shipping);
    }
    @RequestMapping("del.do")
    @ResponseBody
    ServerResponse delete(HttpServletRequest request, Integer shippingId){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  shippingService.delete(shippingId);
    }
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpServletRequest request, Shipping shipping){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  shippingService.update(user.getId(),shipping);
    }
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpServletRequest request,Integer shippingId){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return shippingService.select(shippingId);
    }
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                         HttpServletRequest request){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return shippingService.list(user.getId(),pageNum,pageSize);
    }



}
