package com.hjs.mapper;

import com.hjs.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjs
 * @since 2022-04-28
 */
@Mapper
@Repository
public interface BlogMapper extends BaseMapper<Blog> {

}
