package com.music.common.user.domain.vo.response.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * Description: 好友校验
 * Author: <a href="https://github.com/emberff">pf</a>
 * Date: 2023-03-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteFriendResp extends FriendResp{

    @ApiModelProperty("是否已被邀请")
    private Integer isInvited;
    
}
