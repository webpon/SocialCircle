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



    public static <C> Result<C> ok(String msg, C data, Integer code){
        return new Result<C>(code,msg,data);
    }

    public static <C> Result<C> ok(C data, Integer code){
        return new Result<C>(code,null,data);
    }


    public static <C> Result<C> ok(){
        return new Result<C>(ResultCode.SUCCEED,null,null);
    }



    public static  <C> Result<C> error(String msg){
        return new Result<C>(400,msg,null);
    }

}
