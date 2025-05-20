package com.music.common.music.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-20
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("post_comment")
public class PostComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    @TableField("post_id")
    private Long postId;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 0隐藏 1正常
     */
    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


}
