package com.hjs.controller;


import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hjs.common.Result;
import com.hjs.entity.User;
import com.hjs.service.UserService;
import com.hjs.service.impl.UserServiceImpl;
import com.hjs.util.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjs
 * @since 2022-04-28
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/index")
    @RequiresAuthentication //登陆拦截注解
    public Result<User> getById(){
        User byId = userService.getById(1);
        return Result.success(200,"操作成功",byId);
    }

    /**
     *
     *@RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
     * GET方式无请求体，所以使用@RequestBody接收数据时，
     * 前端不能使用GET方式提交数据，
     * 而是用POST方式进行提交。在后端的同一个接收方法里，
     * @RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个，
     * 而@RequestParam()可以有多个。
     *
     * @Validated注解用于检查user中填写的规则  如果不满足抛出异常
     * 可在GlobalExceptionHandler中捕获此异常 进行自定义 返回数据信息
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user){

        user.setUsername(user.getUsername());
        user.setAvatar(user.getAvatar());
        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setStatus(0);
        user.setCreated(LocalDateTime.now());
        boolean save = userService.saveOrUpdate(user);
        if (save){
            return Result.success(user);
        }else {
            return Result.fail("错误!");
        }

    }

    @RequiresAuthentication
    @GetMapping("/test")
    public Result Test(){
        User all = userService.findAll();
        return Result.success(all);
    }

    @RequiresAuthentication
    @GetMapping("/getUser")
    public Result<List<User>> getUser(){
        List<User> list = userService.list();
        return Result.success(list);
    }
}
