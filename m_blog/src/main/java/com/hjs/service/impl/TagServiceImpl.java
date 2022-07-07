package com.hjs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hjs.entity.Tag;
import com.hjs.mapper.TagMapper;
import com.hjs.service.TagService;
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
 * @since 2022-05-07
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;

//    @Autowired
//    private TagService tagService;

    @Override
    public List<Tag> getAll() {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select();
        return tagMapper.selectList(queryWrapper);
//        return tagService.list(queryWrapper);
    }
}
