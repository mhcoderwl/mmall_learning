package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService cartService;
    @RequestMapping("list.do")
    @ResponseBody
    ServerResponse<CartVO> getProductList(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.getProductList(user.getId());
    }
    @RequestMapping("add.do")
    @ResponseBody
    ServerResponse<CartVO> add(HttpSession session, Integer ProductId,Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.add(user.getId(),ProductId,count);
    }
    @RequestMapping("update.do")
    @ResponseBody
    ServerResponse<CartVO> update(HttpSession session, Integer ProductId,Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.update(user.getId(),ProductId,count);
    }
    @RequestMapping("delete_product.do")
    @ResponseBody
    ServerResponse<CartVO> delete_product(HttpSession session, String ProductIds){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.deleteProduct(user.getId(),ProductIds);
    }
    @RequestMapping("select.do")
    @ResponseBody
    ServerResponse<CartVO> select(HttpSession session, Integer ProductId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.selectOrUnSelect(user.getId(),ProductId,Const.Cart.CHECKED);
    }
    @RequestMapping("un_select.do")
    @ResponseBody
    ServerResponse<CartVO> unselect(HttpSession session, Integer ProductId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.selectOrUnSelect(user.getId(),ProductId,Const.Cart.UN_CHECKED);
    }
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    ServerResponse<Integer> getCartProductCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.getCartProductCount(user.getId());
    }
    @RequestMapping("select_all.do")
    @ResponseBody
    ServerResponse selectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }
    @RequestMapping("un_select_all.do")
    @ResponseBody
    ServerResponse unselectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getStatus(),"用户未登录,请登录管理员");
        }
        return  cartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }
}
