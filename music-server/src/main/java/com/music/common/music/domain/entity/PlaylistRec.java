package com.music.common.music.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@TableName("playlist_rec")
public class PlaylistRec implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌曲id
     */
      @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 歌曲来源 0用户上传 1jamendoApi
     */
    @TableField(value = "rec_playlist_id")
    private Long recPlaylistId;


}
