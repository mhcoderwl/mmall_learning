package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JedisUtil;
import com.mmall.util.JsonUtil;
import com.mmall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService cartService;
    @RequestMapping("list.do")
    @ResponseBody
    ServerResponse<CartVO> getProductList(HttpServletRequest request){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.getProductList(user.getId());
    }
    @RequestMapping("add.do")
    @ResponseBody
    ServerResponse<CartVO> add(HttpServletRequest request, Integer ProductId,Integer count){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.add(user.getId(),ProductId,count);
    }
    @RequestMapping("update.do")
    @ResponseBody
    ServerResponse<CartVO> update(HttpServletRequest request, Integer ProductId,Integer count){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.update(user.getId(),ProductId,count);
    }
    @RequestMapping("delete_product.do")
    @ResponseBody
    ServerResponse<CartVO> delete_product(HttpServletRequest request, String ProductIds){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.deleteProduct(user.getId(),ProductIds);
    }
    @RequestMapping("select.do")
    @ResponseBody
    ServerResponse<CartVO> select(HttpServletRequest request, Integer ProductId){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.selectOrUnSelect(user.getId(),ProductId,Const.Cart.CHECKED);
    }
    @RequestMapping("un_select.do")
    @ResponseBody
    ServerResponse<CartVO> unselect(HttpServletRequest request, Integer ProductId){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.selectOrUnSelect(user.getId(),ProductId,Const.Cart.UN_CHECKED);
    }
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    ServerResponse<Integer> getCartProductCount(HttpServletRequest request){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.getCartProductCount(user.getId());
    }
    @RequestMapping("select_all.do")
    @ResponseBody
    ServerResponse selectAll(HttpServletRequest request){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }
    @RequestMapping("un_select_all.do")
    @ResponseBody
    ServerResponse unselectAll(HttpServletRequest request){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        return  cartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }
}
