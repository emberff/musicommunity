package com.music.common.music.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 歌曲id
     */
    @TableField("song_id")
    private Long songId;

    /**
     * 父id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 内容
     */
    @TableField("cotent")
    private String cotent;

    /**
     * 是否禁用
     */
    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
