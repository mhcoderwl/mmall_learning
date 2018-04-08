package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable{
    String msg;
    T data;
    int status;
    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status,T data){
        this(status);
        this.data=data;
    }
    private ServerResponse(int status,String msg){
        this(status);
        this.msg=msg;
    }
    private ServerResponse(int status,String msg,T data){
        this(status,data);
        this.msg=msg;
    }

    public T getData() {
        return data;
    }
    public int getStatus(){return status;}
    public String getMsg(){return msg;}
    @JsonIgnore
    public boolean isSuccess(){
        return status==ResponseCode.SUCCESS.getStatus();
    }
    static public<T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getStatus());
    }
    static public<T> ServerResponse<T> createBySuccess(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getStatus(),msg);
    }
    static public<T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getStatus(),data);
    }
    static public<T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getStatus(),msg,data);
    }
    static public<T> ServerResponse<T> createByError(){
            return new ServerResponse<T>(ResponseCode.ERROR.getStatus());
    }
    static public<T> ServerResponse<T> createByErrorMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.ERROR.getStatus(), msg);
    }
    static public<T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String msg){
        return new ServerResponse<T>(errorCode,msg);
    }

}
