package com.hjs.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    //200 正常  400 异常
    private Integer code;
    private String msg;
    //返回数据
    private T data;

    //成功
    public static<T> Result<T> success( T data){

        return success(200,"操作成功",data);
    }
    //成功
    public static<T> Result<T> success(Integer code,String msg,T data){
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    //失败
    public static Result fail(String msg){
        return fail(400,msg,null);
    }
    //失败
    public static<T> Result<T> fail(String msg,T data){
        return fail(400,msg,data);
    }
    //失败
    public static <T> Result<T> fail(Integer code,String msg,T data){
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
