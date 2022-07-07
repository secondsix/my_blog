package com.hjs.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjs.common.Result;
import com.hjs.config.RedisKeyConfig;
import com.hjs.entity.Tag;
import com.hjs.mapper.TagMapper;
import com.hjs.service.RedisService;
import com.hjs.service.TagService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hjs
 * @since 2022-05-07
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @Autowired
    private RedisService redisService;

    /**
     * 查询所有标签
     * @return
     */
    @GetMapping("/getTagAll")
    public Result getAll() {
//        System.out.println("执行了此方法");
//        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select();
        if (redisService.hasHashKey(RedisKeyConfig.TAG_NAME_CACHE,RedisKeyConfig.ALL)){
            return Result.success(redisService.getValueByHashKey(RedisKeyConfig.TAG_NAME_CACHE,RedisKeyConfig.ALL));
        }
        List<Tag> list = tagService.getAll();
//        System.out.println("list = " + list);
        redisService.saveKVToHash(RedisKeyConfig.TAG_NAME_CACHE,RedisKeyConfig.ALL,list);
        return Result.success(list);
    }

    /**
     * 分页查询所有标签
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/getPageTag")
    public Result getPageTag(@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                            @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
        Page<Tag> page = new Page<>(pageNo,pageSize);
        IPage<Tag> iPage = tagService.page(page);
        return Result.success(iPage);
    }

    /**
     * 新增标签
     * @param tag
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/tagSave")
    public Result tagSave(@Validated @RequestBody Tag tag){
        boolean save = tagService.save(tag);
        redisService.deleteCacheByKey(RedisKeyConfig.TAG_NAME_CACHE);
        if (save){
            return Result.success("success");
        }else{
            return Result.fail("fail");
        }
    }

    /**
     * 修改标签
     * @param tag
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/tagUpdate")
    public Result tagUpdate(@Validated @RequestBody Tag tag){
        Tag byId = tagService.getById(tag.getId());
        byId.setTag(tag.getTag());
        boolean update = tagService.saveOrUpdate(byId);
        redisService.deleteCacheByKey(RedisKeyConfig.TAG_NAME_CACHE);
        if (update){
            return Result.success("success");
        }else {
            return Result.fail("fail");
        }
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/delTag/{id}")
    public Result delTag(@PathVariable(name = "id") Long id){
        boolean b = tagService.removeById(id);
        redisService.deleteCacheByKey(RedisKeyConfig.TAG_NAME_CACHE);
        if (b){
            return Result.success("success");
        }else {
            return Result.fail("fail");
        }
    }
}
