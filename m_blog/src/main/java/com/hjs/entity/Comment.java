package com.hjs.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author hjs
 * @since 2022-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 头像（图片路径）
     */
    private String avatar;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 评论者ip地址
     */
    private String ip;

    /**
     * 博主回复
     */
    private Integer isAdminComment;

    /**
     * 公开或非公开
     */
    private Boolean isPublished;

    /**
     * 所属的文章
     */
    private Long blogId;

    /**
     * 父评论id，-1为根评论
     */
    private Long parentCommentId;

    /**
     * 个人网站
     */
    private String website;

    /**
     * 被回复昵称
     */
    private String parentCommentNickname;

    /**
     * 如果评论昵称为qq号，则将昵称和头像重置为qq昵称和头像
     */
    private String qq;


}
