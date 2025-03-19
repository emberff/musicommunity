package com.music.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author <a href="https://github.com/emberff">emberff</a>
 * @since 2023-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Long UID_SYSTEM = 1L; // 系统用户 ID

    /**
     * 用户 ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户类型 0 管理员, 1 普通用户
     */
    @TableField("type")
    private Integer type;

    /**
     * 手机号（注册用，唯一）
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户昵称
     */
    @TableField("name")
    private String name;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 性别 1 男性, 2 女性
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 在线状态 1 在线, 2 离线
     */
    @TableField("active_status")
    private Integer activeStatus;

    /**
     * 最后上下线时间
     */
    @TableField("last_opt_time")
    private Date lastOptTime;

    /**
     * IP 信息（JSON）
     */
    @TableField(value = "ip_info", typeHandler = JacksonTypeHandler.class)
    private IpInfo ipInfo;

    /**
     * 用户状态 0 正常, 1 拉黑
     */
    @TableField("status")
    private Integer status;

    /**
     * 用户标签
     */
    @TableField("user_tags")
    private String userTags;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 刷新 IP 信息
     */
    public void refreshIp(String ip) {
        if (ipInfo == null) {
            ipInfo = new IpInfo();
        }
        ipInfo.refreshIp(ip);
    }
}
