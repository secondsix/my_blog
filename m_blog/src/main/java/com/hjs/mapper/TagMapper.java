package com.hjs.mapper;

import com.hjs.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjs
 * @since 2022-05-07
 */
@Mapper
@Repository
public interface TagMapper extends BaseMapper<Tag> {
}
