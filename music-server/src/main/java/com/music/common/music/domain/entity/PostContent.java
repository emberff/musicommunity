package com.music.common.music.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户自制发布内容表: 1对多
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-20
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("post_content")
public class PostContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId("id")
    private Long id;

    /**
     * 发布文章id
     */
    @TableField("post_id")
    private Long postId;

    /**
     * 媒体文件, 支持图片, 语音, 视频
     */
    @TableField("url")
    private String url;

    @TableField("sort")
    private Integer sort;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
