package com.hjs.service.impl;

import com.hjs.entity.Type;
import com.hjs.mapper.TypeMapper;
import com.hjs.service.TypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjs
 * @since 2022-05-07
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

}
