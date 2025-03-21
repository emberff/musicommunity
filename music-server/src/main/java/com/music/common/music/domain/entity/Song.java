package com.music.common.music.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 歌曲表
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("song")
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌曲id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌手id
     */
    @TableField("singer_id")
    private Long singerId;

    /**
     * 专辑id
     */
    @TableField("playlist_id")
    private Long playlistId;

    /**
     * 歌曲名
     */
    @TableField("name")
    private String name;

    /**
     * 封面url
     */
    @TableField("cover")
    private String cover;

    /**
     * 歌曲url
     */
    @TableField("url")
    private String url;

    /**
     * 上传用户id
     */
    @TableField("uploader_id")
    private Long uploaderId;

    /**
     * 是否禁用 1正常 0禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


}
