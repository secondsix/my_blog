package com.hjs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hjs.entity.User;
import com.hjs.mapper.UserMapper;
import com.hjs.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjs
 * @since 2022-04-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findAll() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select();
        return userMapper.selectOne(queryWrapper);
    }
}
