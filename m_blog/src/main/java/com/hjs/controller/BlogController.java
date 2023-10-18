package com.hjs.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjs.common.Result;
import com.hjs.config.RedisKeyConfig;
import com.hjs.entity.Blog;
import com.hjs.entity.Tag;
import com.hjs.entity.Type;
import com.hjs.entity.User;
import com.hjs.service.*;
import com.hjs.shiro.ShiroUtil;
import com.hjs.util.FastDFSClient;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hjs
 * @since 2022-04-28
 */
@RestController
@RequestMapping("blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    /**
     * 获取所有博客并以创建时间排序
     *
     * @param currentPage
     * @return
     */
    //没有默认值为1
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.success(pageData);
    }

    /**
     * 根据id获取博客
     *
     * @param id
     * @return
     */
    //@PathVariable 动态路由
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Blog blog = blogService.getById(id);
        //判断是否为空 为空则断言异常
        Assert.notNull(blog, "该博客已被删除");
        return Result.success(blog);
    }

    /**
     * 根据id获取博客，并添加分类名称、标签名称
     *
     * @param id
     * @return
     */
    //@PathVariable 动态路由
    @GetMapping("/blogs/{id}")
    public Result blogDetail(@PathVariable Long id) {
        Blog blog = blogService.getById(id);

        List<Type> typeList = typeService.list();
        List<Tag> tagList = tagService.list();

        for (int i = 0; i < typeList.size(); i++) {
            if (blog.getTypeId().equals(typeList.get(i).getId())) {
                blog.setTypeName(typeList.get(i).getTypeName());
            }
        }

        for (int i = 0; i < tagList.size(); i++) {
            if (blog.getTagId().equals(tagList.get(i).getId())) {
                blog.setTagName(tagList.get(i).getTag());
            }
        }

        //判断是否为空 为空则断言异常
        Assert.notNull(blog, "该博客已被删除");
        if (blog.getStatus() != 1) {
            return Result.fail("你没有权限查阅此博客");
        }

        if (redisService.getMapByHash(RedisKeyConfig.BLOG_VIEWS_MAP).containsKey(id)) {
            redisService.incrementByHashKey(RedisKeyConfig.BLOG_VIEWS_MAP, id, 1);
        } else {
            redisService.saveKVToHash(RedisKeyConfig.BLOG_VIEWS_MAP, id, 1);
        }


        return Result.success(blog);
    }

    /**
     * 编辑博客及创建博客
     *
     * @param blog
     * @return
     */
    //添加删除 没有id则添加 有id则编辑
    @RequiresAuthentication //需要认证之后才能操作
    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        //一个空对象用于赋值
        Blog temp = null;
        //如果有id则是编辑
        if (blog.getId() != null) {
            temp = blogService.getById(blog.getId());//将数据库的内容传递给temp
            temp.setUpdateTime(LocalDateTime.now());

            if (blog.getTypeId() != null) {
                temp.setTypeId(blog.getTypeId());
            } else {
                temp.setTypeId(null);
            }
            if (blog.getTagId() != null) {
                temp.setTagId(blog.getTagId());
            } else {
                temp.setTagId(null);
            }


            //只能编辑自己的文章
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有编辑权限");
        } else {
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setUpdateTime(LocalDateTime.now());
            temp.setStatus(blog.getStatus());
        }
//        System.out.println("temp = " + temp);
        //将blog的值赋给temp，忽略id userid created status 引用于hutool
        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status", "updateTime");
//        System.out.println("temp = " + temp);
        boolean b = blogService.saveOrUpdate(temp);
        redisService.deleteCacheByKey(RedisKeyConfig.BLOG_INFO_CACHE);
        redisService.deleteCacheByKey(RedisKeyConfig.BLOG_SYSTEM_INFO_CACHE);
        redisService.deleteCacheByKey(RedisKeyConfig.ARCHIVE_INFO_CACHE);
        redisService.deleteCacheByKey(RedisKeyConfig.CATEGORY_BLOG_CACHE);
        redisService.deleteCacheByKey(RedisKeyConfig.TAG_BLOG_CACHE);
        if (b) {
            return Result.success("操作成功!");
        } else {
            return Result.fail("操作失败!");
        }

    }

    /**
     * 根据id删除博客
     *
     * @param id
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blogdel/{id}")
    public Result del(@PathVariable Long id) {
        boolean b = blogService.removeById(id);

        //判断是否为空，为空则断言异常
        if (b) {
            redisService.deleteCacheByKey(RedisKeyConfig.BLOG_INFO_CACHE);
            redisService.deleteCacheByKey(RedisKeyConfig.BLOG_SYSTEM_INFO_CACHE);
            redisService.deleteCacheByKey(RedisKeyConfig.ARCHIVE_INFO_CACHE);
            redisService.deleteCacheByKey(RedisKeyConfig.CATEGORY_BLOG_CACHE);
            redisService.deleteCacheByKey(RedisKeyConfig.TAG_BLOG_CACHE);
            return Result.success("文章删除成功");
        } else {
            return Result.fail("文章删除失败");
        }
    }

    /**
     * 根据状态获取博客，并添加分类名称、标签名称、用户名，按创建时间排序
     *
     * @param pageNo
     * @param pageSize
     * @param status
     * @return
     */
//    @RequiresAuthentication
    @GetMapping("/getData")
    public Result create(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            Integer status) {
        //获取所有博客

        if (status != null) {
            if (redisService.hasHashKey(RedisKeyConfig.BLOG_INFO_CACHE, pageNo)) {
                return Result.success(redisService.getValueByHashKey(RedisKeyConfig.BLOG_INFO_CACHE, pageNo));
            }
        } else {
            if (redisService.hasHashKey(RedisKeyConfig.BLOG_SYSTEM_INFO_CACHE, pageNo)) {
                return Result.success(redisService.getValueByHashKey(RedisKeyConfig.BLOG_SYSTEM_INFO_CACHE, pageNo));
            }
        }

        //获取所有分类
        List<Type> typeList = typeService.list(new QueryWrapper<Type>().select());
        //获取所有标签
        List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().select());
        //获取所有用户
        List<User> userList = userService.list(new QueryWrapper<User>().select());

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        if (status != null) {
            queryWrapper.eq("status", status)
                    .orderByDesc("created");
        } else {
            queryWrapper.orderByDesc("created");
        }

        Page<Blog> page = new Page<>(pageNo, pageSize);
        IPage<Blog> blogIPage = blogService.page(page, queryWrapper);
        for (int i = 0; i < blogIPage.getRecords().size(); i++) {
            //设置分类名称
            for (int j = 0; j < typeList.size(); j++) {
                if (blogIPage.getRecords().get(i).getTypeId().equals(typeList.get(j).getId())) {
                    blogIPage.getRecords().get(i).setTypeName(typeList.get(j).getTypeName());
                }
            }

            //设置标签名称
            for (int j = 0; j < tagList.size(); j++) {
                if (blogIPage.getRecords().get(i).getTagId().equals(tagList.get(j).getId())) {
                    blogIPage.getRecords().get(i).setTagName(tagList.get(j).getTag());
                }
            }
            //设置用户名
            for (int j = 0; j < userList.size(); j++) {
                if (blogIPage.getRecords().get(i).getUserId().equals(userList.get(j).getId())) {
                    blogIPage.getRecords().get(i).setUsername(userList.get(j).getUsername());
                }
            }
        }

        if (status != null) {
            redisService.saveKVToHash(RedisKeyConfig.BLOG_INFO_CACHE, pageNo, blogIPage);
        } else {
            redisService.saveKVToHash(RedisKeyConfig.BLOG_SYSTEM_INFO_CACHE, pageNo, blogIPage);
        }

        return Result.success(blogIPage);
    }

    /**
     * 修改状态值
     *
     * @param id
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/show")
    public Result Show(@RequestParam(name = "id") Long id) {
        Blog byId = blogService.getById(id);
        if (byId.getStatus() == 0) {
            byId.setStatus(1);
        } else {
            byId.setStatus(0);
        }
        boolean save = blogService.saveOrUpdate(byId);
        redisService.deleteCacheByKey(RedisKeyConfig.BLOG_INFO_CACHE);
        redisService.deleteCacheByKey(RedisKeyConfig.BLOG_SYSTEM_INFO_CACHE);
        redisService.deleteCacheByKey(RedisKeyConfig.ARCHIVE_INFO_CACHE);
        redisService.deleteCacheByKey(RedisKeyConfig.CATEGORY_BLOG_CACHE);
        redisService.deleteCacheByKey(RedisKeyConfig.TAG_BLOG_CACHE);
        return Result.success(save);
    }

    /**
     * 查询 关于我 的博客
     *
     * @return
     */
    @GetMapping("/about")
    public Result getAbout() {
        if (redisService.hasHashKey(RedisKeyConfig.BLOG_INFO_CACHE, RedisKeyConfig.ABOUT_INFO_CACHE)) {
//            System.out.println("redisService = " + redisService.getValueByHashKey(RedisKeyConfig.BLOG_INFO_CACHE,RedisKeyConfig.ABOUT_INFO_CACHE));
            return Result.success(redisService.getValueByHashKey(RedisKeyConfig.BLOG_INFO_CACHE, RedisKeyConfig.ABOUT_INFO_CACHE));
        }
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq("title", "关于我!!!");
        Blog one = blogService.getOne(blogQueryWrapper);
//        System.out.println("one = " + one);
        redisService.saveKVToHash(RedisKeyConfig.BLOG_INFO_CACHE, RedisKeyConfig.ABOUT_INFO_CACHE, one);
        return Result.success(200, "操作成功!", one);
    }

    /**
     * 根据分类名称查询博客
     *
     * @param currentPage
     * @param pageSize
     * @param typeName
     * @return
     */
    @GetMapping("/byBlogTypeName")
    public Result getByBlogTypeName(@RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                    @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                                    String typeName) {
        if (redisService.hasHashKey(RedisKeyConfig.CATEGORY_BLOG_CACHE, typeName + currentPage)) {
            return Result.success(redisService.getValueByHashKey(RedisKeyConfig.CATEGORY_BLOG_CACHE, typeName + currentPage));
        }
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.eq("type_name", typeName);
        Type type = typeService.getOne(typeQueryWrapper);

        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq("status", 1)
                .eq("type_id", type.getId())
                .orderByDesc("created");

        Page<Blog> page = new Page<>(currentPage, pageSize);
        IPage<Blog> blogIPage = blogService.page(page, blogQueryWrapper);

        redisService.saveKVToHash(RedisKeyConfig.CATEGORY_BLOG_CACHE, typeName + currentPage, blogIPage);
        return Result.success(blogIPage);
    }

    /**
     * 根据标签名查询博客
     *
     * @param currentPage
     * @param pageSize
     * @param tagName
     * @return
     */
    @GetMapping("/byBlogTagName")
    public Result getByBlogTagName(@RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                   @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                                   String tagName) {
        if (redisService.hasHashKey(RedisKeyConfig.TAG_BLOG_CACHE, tagName + currentPage)) {
            return Result.success(redisService.getValueByHashKey(RedisKeyConfig.TAG_BLOG_CACHE, tagName + currentPage));
        }
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("tag", tagName);
        Tag tag = tagService.getOne(tagQueryWrapper);

        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq("status", 1)
                .eq("tag_id", tag.getId())
                .orderByDesc("created");

        Page<Blog> page = new Page<>(currentPage, pageSize);
        IPage<Blog> blogIPage = blogService.page(page, blogQueryWrapper);

        redisService.saveKVToHash(RedisKeyConfig.TAG_BLOG_CACHE, tagName + currentPage, blogIPage);
        return Result.success(blogIPage);

    }

    /**
     * 归档查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/archives")
    public Result getArchives(@RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (redisService.hasHashKey(RedisKeyConfig.ARCHIVE_INFO_CACHE, currentPage)) {
            return Result.success(redisService.getValueByHashKey(RedisKeyConfig.ARCHIVE_INFO_CACHE, currentPage));
        }
        Page<Blog> page = new Page<>(currentPage, pageSize);
        IPage<Blog> iPage = blogService.page(page, new QueryWrapper<Blog>().eq("status", 1).orderByDesc("created"));

        redisService.saveKVToHash(RedisKeyConfig.ARCHIVE_INFO_CACHE, currentPage, iPage);
        return Result.success(iPage);
    }

    /**
     * 通过id查询博客，将博客中图片地址及服务器上对应的地址删除
     * @param id
     * @return
     */
    @PostMapping("/deleteDImg")
    public Result<?> deleteImg(String id) throws Exception {
        Blog blog = blogService.getById(id);
        if (blog.getDescription() != null){
            String description = blog.getDescription();
            String content = blog.getContent();

            if (description.contains("/group1/M00/00/00/")){
                List<String> stringLists = stringList(description);
                for (String i: stringLists) {
                    FastDFSClient.deleteFile("group1", "M00/00/00/" + i);
                }
            }

            if (content.contains("/group1/M00/00/00/")){
                List<String> stringList = stringList(content);
                for (String i: stringList) {
                    FastDFSClient.deleteFile("group1", "M00/00/00/" + i);
                }
            }
        }

        return Result.success("success");
    }

    /**
     * 解析图片地址工具类
     * @param bb
     * @return
     */
    public List<String> stringList(String bb){
        char[] chars = bb.toCharArray();
        StringBuffer stringBuffer=new StringBuffer();
        boolean a=false;
        for (char aChar : chars) {
            if(aChar=='('){
                a=true;
            }
            if(a){
                stringBuffer.append(aChar);
            }
            if(aChar==')'){
                a=false;
            }
        }
        String string = stringBuffer.toString();
        String[] split = string.split("\\)");
        LinkedList<String> linkedList = new LinkedList<>();
        for (String i : split) {
            String substr=i.substring(i.lastIndexOf("/")+1);
            linkedList.add(substr);
        }
        return linkedList;
    }


    @PostMapping("/search")
    public Result<?> search(String queryString){
        List<Blog> list = blogService.list(new QueryWrapper<Blog>().like("content", queryString).eq("status", 1).orderByDesc("created"));
        return Result.success(list);
    }
}
