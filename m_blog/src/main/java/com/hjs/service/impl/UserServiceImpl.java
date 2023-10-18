package com.hjs.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hjs.entity.User;
import com.hjs.mapper.UserMapper;
import com.hjs.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    private UserService userService;

    @Override
    public User findAll() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select();
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int updatePassword(String username,String oldPassword, String checkPassword) {
        User username1 = userService.getOne(new QueryWrapper<User>().eq("username", username));

        if (SecureUtil.md5(oldPassword).equals(username1.getPassword())){
            username1.setPassword(SecureUtil.md5(checkPassword));
            userService.updateById(username1);
            return 1;
        }else {
            return 0;
        }
    }
}
