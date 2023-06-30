package com.button.demo1.exception;

import com.button.demo1.utils.HttpCodeEnum;

public class BusinessException extends RuntimeException {

    private int code;
    //使用枚举构造
    public BusinessException(HttpCodeEnum httpCodeEnum){
        super(httpCodeEnum.getMsg());
        this.code=httpCodeEnum.getCode();
    }
    //使用自定义消息体
    public BusinessException(HttpCodeEnum httpCodeEnum,String msg){
        super(msg);
        this.code=httpCodeEnum.getCode();
    }

    //根据异常构造
    public BusinessException(HttpCodeEnum httpCodeEnum,Throwable msg){
        super(msg);
        this.code=httpCodeEnum.getCode();
    }




}
