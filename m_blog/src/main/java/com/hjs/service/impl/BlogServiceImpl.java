package com.hjs.service.impl;


import com.hjs.entity.Blog;
import com.hjs.mapper.BlogMapper;
import com.hjs.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjs
 * @since 2022-04-28
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
