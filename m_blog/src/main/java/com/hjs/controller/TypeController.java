package com.hjs.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjs.common.Result;
import com.hjs.config.RedisKeyConfig;
import com.hjs.entity.Tag;
import com.hjs.entity.Type;
import com.hjs.service.RedisService;
import com.hjs.service.TypeService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjs
 * @since 2022-05-07
 */
@RestController
@RequestMapping("/type")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @Autowired
    private RedisService redisService;

    /**
     * 查询所有分类
     * @return
     */
    @GetMapping("/getTypeAll")
    public Result getAll(){
        if (redisService.hasHashKey(RedisKeyConfig.CATEGORY_NAME_CACHE,RedisKeyConfig.ALL)){
            return Result.success(redisService.getValueByHashKey(RedisKeyConfig.CATEGORY_NAME_CACHE,RedisKeyConfig.ALL));
        }
        List<Type> list = typeService.list();
        redisService.saveKVToHash(RedisKeyConfig.CATEGORY_NAME_CACHE,RedisKeyConfig.ALL,list);
        return Result.success(list);
    }

    /**
     * 分页查询分类
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/getPageAll")
    public Result getPageAll(@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
        Page<Type> page = new Page<>(pageNo,pageSize);
        IPage<Type> iPage= typeService.page(page,new QueryWrapper<Type>().select());
//        System.out.println("iPage.getRecords() = " + iPage.getRecords());
        return Result.success(iPage);
    }

    /**
     * 修改分类
     * @param type
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/update")
    public Result TypeUpdate(@Validated @RequestBody Type type){
//        System.out.println("type = " + type);
        Type byId = typeService.getById(type.getId());
        byId.setTypeName(type.getTypeName());
        boolean b = typeService.saveOrUpdate(byId);
        redisService.deleteCacheByKey(RedisKeyConfig.CATEGORY_NAME_CACHE);
        if (b){
            return Result.success("success");
        }else {
            return Result.fail("fail");
        }
    }

    /**
     * 新增分类
     * @param type
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/save")
    public Result TypeSave(@Validated @RequestBody Type type){
//        System.out.println("type = " + type);
        boolean save = typeService.save(type);
        redisService.deleteCacheByKey(RedisKeyConfig.CATEGORY_NAME_CACHE);
        if (save){
            return Result.success("success");
        }else{
            return Result.fail("fail");
        }
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/del/{id}")
    public Result Delete(@PathVariable(name = "id") Long id){
        boolean b = typeService.removeById(id);
        redisService.deleteCacheByKey(RedisKeyConfig.CATEGORY_NAME_CACHE);
        if (b){
            return Result.success("success");
        }else{
            return Result.fail("fail");
        }
    }
}
