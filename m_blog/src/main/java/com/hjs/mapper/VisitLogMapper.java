package com.hjs.mapper;

import com.hjs.entity.VisitLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
public interface VisitLogMapper extends BaseMapper<VisitLog> {

}
