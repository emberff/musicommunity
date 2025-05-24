package com.music.common.music.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户关注歌手表
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_follow")
public class UserFollow implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌手id
     */
    @TableField("singer_id")
    private Long singerId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;


}
