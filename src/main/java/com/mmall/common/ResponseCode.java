package com.mmall.common;

/*
    用来存放一些状态信息，不用硬编码的方式方便修改。
 */
public enum ResponseCode {
    SUCCESS(0,"success"),
    ERROR(1,"failed"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");
    private final int status;
    private final String msg;
    ResponseCode(int status,String msg){
        this.msg=msg;
        this.status=status;
    }
    public int getStatus(){
        return this.status;
    }
    public String getDesc(){
        return this.msg;
    }


}
