package com.hjs.service;

import com.hjs.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjs
 * @since 2022-04-28
 */
public interface UserService extends IService<User> {
    public User findAll();
}
