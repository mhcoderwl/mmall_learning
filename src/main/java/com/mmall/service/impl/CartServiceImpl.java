package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVO;
import com.mmall.vo.CartVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements ICartService{
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Override
    public ServerResponse<CartVO> getProductList(Integer userId) {
        return ServerResponse.createBySuccess(getCartVoLimit(userId));
    }

    @Override
    public ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //判断商品是否已经在购物车中
        //判断商品数量是否超出了库存
        Cart cart = cartMapper.selectByProductIdUserId(userId, productId);
        if (cart == null) {
            cart = new Cart();
            Product product = productMapper.selectByPrimaryKey(productId);
            cart.setQuantity(count);
            cart.setChecked(Const.Cart.CHECKED);
            cart.setUserId(userId);
            cart.setProductId(productId);
            cartMapper.insert(cart);
        } else {
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return getProductList(userId);
    }

    @Override
    public ServerResponse<CartVO> update(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //判断商品是否已经在购物车中
        //判断商品数量是否超出了库存
        Cart cart = cartMapper.selectByProductIdUserId(userId, productId);
        if (cart != null) {
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return getProductList(userId);
    }
    public ServerResponse<CartVO> deleteProduct(Integer userId,String productIds){
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getStatus(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productIdList);
        return getProductList(userId);
    }
    public ServerResponse<CartVO> selectOrUnSelect (Integer userId,Integer productId,Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return getProductList(userId);
    }
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }
    private CartVO getCartVoLimit(Integer userId){
        CartVO cartVO=new CartVO();
        List cartProductVOList= Lists.newArrayList();
        cartVO.setCartProductVOList(cartProductVOList);
        BigDecimal cartTotalPrice = new BigDecimal("0");
        List<Cart> cartList=cartMapper.getListByUserId(userId);
        if(cartList!=null&&!cartList.isEmpty()) {
            for (Cart cart : cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                Integer productId=cart.getProductId();
                Product product=productMapper.selectByPrimaryKey(productId);
                cartProductVO.setProductId(productId);
                cartProductVO.setProductSubtitle(product.getSubtitle());
                cartProductVO.setProductPrice(product.getPrice());
                cartProductVO.setProductName(product.getName());
                cartProductVO.setProductStock(product.getStock());
                cartProductVO.setProductMainImage(product.getMainImage());
                int buyLimitCount = 0;
                if(product.getStock() >= cart.getQuantity()){
                    //库存充足的时候
                    buyLimitCount = cart.getQuantity();
                    cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                }else{
                    buyLimitCount = product.getStock();
                    cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                    //购物车中更新有效库存,因为库存可能在结账时和添加时不一样
                    Cart cartForQuantity = new Cart();
                    cartForQuantity.setId(cart.getId());
                    cartForQuantity.setQuantity(buyLimitCount);
                    cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                }
                cartProductVO.setQuantity(buyLimitCount);
                cartProductVO.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVO.getQuantity()));
                cartProductVO.setProductChecked(cart.getChecked());
                if(cart.getChecked() == Const.Cart.CHECKED){
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setAllChecked(this.getAllCheckedStatus(userId));
        cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVO;
    }
    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectNotCheckedByUserId(userId) == 0;
    }
}
