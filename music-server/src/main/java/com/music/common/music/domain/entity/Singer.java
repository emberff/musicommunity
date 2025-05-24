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
 * 歌手表
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("singer")
public class Singer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌手id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 对应的用户id
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 歌手名
     */
    @TableField("name")
    private String name;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 专辑数
     */
    @TableField("album_num")
    private Integer albumNum;

    /**
     * 关注数
     */
    @TableField("follow_num")
    private Integer followNum;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


}
