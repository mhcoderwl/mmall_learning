package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER="current_user";
    public static final String USERNAME="username";
    public static final String EMAIL="email";

    public interface Cart{
         String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
         String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";
         int CHECKED=1;
        int UN_CHECKED = 0;//购物车中未选中状态
    }
    public interface Role{
         int ROLE_CUSTOMER=0;
         int ROLE_ADMIN=1;
    }
    public enum ProductStatusEnum{
        ON_SALE(1,"在售"),
        OUT_SALE(0,"已下架");
         private final int status;
        private final String msg;
         ProductStatusEnum(int status,String msg){
            this.status=status;
            this.msg=msg;
        }
        public int getStatus(){
            return this.status;
        }
        public String getDesc(){
            return this.msg;
        }
    }
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }
}
