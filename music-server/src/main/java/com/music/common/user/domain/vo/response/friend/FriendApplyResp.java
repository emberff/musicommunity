package com.music.common.user.domain.vo.response.friend;

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * Description: 好友校验
 * Author: <a href="https://github.com/emberff">pf</a>
 * Date: 2023-03-23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplyResp {
    @ApiModelProperty("申请id")
    private Long applyId;

    @ApiModelProperty("申请人uid")
    private Long uid;

    @ApiModelProperty("申请人姓名")
    private String name;

    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("申请类型 1加好友")
    private Integer type;

    @ApiModelProperty("申请信息")
    private String msg;

    @ApiModelProperty("申请状态 1待审批 2同意")
    private Integer status;

    @ApiModelProperty("申请时间")
    private Date createTime;
}
