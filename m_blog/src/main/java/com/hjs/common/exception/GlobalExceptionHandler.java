package com.hjs.common.exception;

import com.hjs.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* 日志输出
* 全局输出异常
* */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)//因为前后端分离 返回一个状态  一般是401没有权限
    @ExceptionHandler(value = ShiroException.class)//捕获运行时异常ShiroException是大部分异常的父类
    public Result handler(ShiroException e){
        log.error("运行时异常：-----------------{}",e);
        return Result.fail(401,e.getMessage(),null);
    }

    /*
    * 实体校验异常
    * MethodArgumentNotValidException 捕获实体校验异常
    * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class) //捕获运行时异常
    public Result handler(MethodArgumentNotValidException e){
        log.error("实体捕获异常：-----------------{}",e);
        BindingResult bindingException = e.getBindingResult();
        //多个异常顺序抛出异常
        ObjectError objectError = bindingException.getAllErrors().stream().findFirst().get();
        return Result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)//捕获运行时的异常
    public Result handler(RuntimeException e){
        log.error("运行时异常：-----------------{}",e);
        return Result.fail(e.getMessage());
    }

    /**
     * 断言异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常：----------------{}",e);
        return Result.fail(e.getMessage());
    }
}
