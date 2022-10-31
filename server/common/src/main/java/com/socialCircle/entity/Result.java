package com.socialCircle.entity;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
    private Long total;

    private Result(){}

    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <C> Result<C> ok(C data){
        return new Result<C>(200,null,data);
    }

    public static <C> Result<C> ok(String msg, C data){
        return new Result<C>(200,msg,data);
    }

    public static <C> Result<C> ok(String msg){
        return new Result<C>(200,msg,null);
    }


    public static <C> Result<C> ok(){
        return new Result<C>(200,null,null);
    }



    public static  <C> Result<C> error(String msg){
        return new Result<C>(400,msg,null);
    }
    public static  <C> Result<C> error(Integer code, String msg){
        return new Result<C>(code,msg,null);
    }
    public static  <C> Result<C> error(C data){
        return new Result<C>(400,null,data);
    }

}
