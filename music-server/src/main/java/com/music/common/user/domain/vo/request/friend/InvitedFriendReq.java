package com.music.common.user.domain.vo.request.friend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.music.common.common.domain.vo.req.CursorPageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * Description: 好友校验
 * Author: <a href="https://github.com/emberff">pf</a>
 * Date: 2023-03-23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitedFriendReq extends CursorPageBaseReq {

    @NotNull(message = "歌单id 不能为空")
    @ApiModelProperty("歌单id")
    private Long playlistId;

    @JsonIgnore
    public Long roomId;

}
