package com.music.common.user.domain.vo.response.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Description: 好友校验
 * Author: <a href="https://github.com/emberff">pf</a>
 * Date: 2023-03-23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendResp {

    @ApiModelProperty("好友uid")
    private Long uid;
    /**
     * @see ChatActiveStatusEnum
     */
    @ApiModelProperty("在线状态 1在线 2离线")
    private Integer activeStatus;

    @ApiModelProperty("个性签名")
    private String sign;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("性别")
    private Integer sex;
}
