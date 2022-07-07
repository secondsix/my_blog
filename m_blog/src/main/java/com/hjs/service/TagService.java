package com.hjs.service;

import com.hjs.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjs
 * @since 2022-05-07
 */
public interface TagService extends IService<Tag> {

    //获取所有分类
    public List<Tag> getAll();
}
