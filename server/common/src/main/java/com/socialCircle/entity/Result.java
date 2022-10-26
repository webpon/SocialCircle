package com.socialCircle.entity;

import com.socialCircle.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

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

    public static  <C> Result<C> error(String msg, Integer code){
        return new Result<C>(code,msg,null);
    }

}
