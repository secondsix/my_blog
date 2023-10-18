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
    /**
     * 查找所有用户
     *
     * @return {@link User}
     */
    public User findAll();

    /**
     * 更新密码
     *
     * @param oldPassword   旧密码
     * @param checkPassword 确认密码
     * @param username      用户名
     * @return int
     */
    int updatePassword(String username,String oldPassword,String checkPassword);
}
