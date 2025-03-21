package com.music.common.music.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 歌单表
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("playlist")
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌单id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌单类型 0专辑 1系统歌单 2用户喜欢 3用户歌单
     */
    @TableField("type")
    private Integer type;

    /**
     * 歌单名
     */
    @TableField("name")
    private String name;

    /**
     * 封面
     */
    @TableField("cover")
    private String cover;

    /**
     * 是否公开 0否 1是
     */
    @TableField("is_public")
    private Integer isPublic;

    /**
     * 歌单歌曲数
     */
    @TableField("pl_song_number")
    private Integer plSongNumber;

    /**
     * 歌单播放数
     */
    @TableField("pl_listen_number")
    private Integer plListenNumber;

    /**
     * 分享数
     */
    @TableField("pl_share_number")
    private Integer plShareNumber;

    /**
     * 评论数
     */
    @TableField("pl_comment_number")
    private Integer plCommentNumber;

    /**
     * 用户关注数
     */
    @TableField("pl_follow_number")
    private Integer plFollowNumber;

    /**
     * 歌单标签
     */
    @TableField("pl_tag")
    private String plTag;

    /**
     * 创建者id
     */
    @TableField("pl_creator_id")
    private Long plCreatorId;

    /**
     * 状态 0删除 1正常
     */
    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
