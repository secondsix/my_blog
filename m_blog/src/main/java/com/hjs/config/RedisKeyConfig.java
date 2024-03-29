package com.hjs.config;

/**
 * redis缓存的key
 */
public class RedisKeyConfig {
    //首页博客简介列表 分页对象key：homeBlogInfoList : {{1,"第一页的缓存",{2,"第二页的缓存"}}
    public static final String BLOG_INFO_CACHE = "BlogInfoCache";

    //后台博客文章列表
    public static final String BLOG_SYSTEM_INFO_CACHE = "BlogSystemInfoCache";

    //分类名列表Key
    public static final String CATEGORY_NAME_CACHE = "categoryNameCache";

    //分类博客列表key
    public static final String CATEGORY_BLOG_CACHE = "categoryBlogCache";

    //标签名列表key
    public static final String TAG_NAME_CACHE = "tagNameCache";

    //标签博客列表key
    public static final String TAG_BLOG_CACHE = "tagBlogCache";

    //关于我页面key
    public static final String ABOUT_INFO_CACHE = "aboutInfoCache";

    //友链信息key
    public static final String FRIEND_INFO_CACHE = "friendInfoCache";

    //友链页面博客信息key
    public static final String FRIEDN_BLOG_CACHE = "friendBlogCache";

    /**
     * 档案信息缓存
     */
    //博客归档key
    public static final String ARCHIVE_INFO_CACHE = "archiveInfoCache";

    //博客访问量key
    public static final String BLOG_VIEWS_MAP = "blogViewsMap";

    //访客标识码key
    public static final String IDENTIFICATION_SET = "identificationSet";

    //不分页key
    public static final String ALL = "all";

    //PVUV
    public static final String PV_UV = "pv_uv";

    //访问key
    public static final String VISIT_API = "visitApi";
}
