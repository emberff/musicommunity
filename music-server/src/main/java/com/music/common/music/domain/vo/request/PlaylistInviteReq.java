package com.music.common.music.domain.vo.request;

import com.music.common.common.domain.vo.req.IdListReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 邀请好友加入歌单管理
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistInviteReq extends IdListReqVO {

    @NotNull
    @ApiModelProperty(value = "歌单id", required = true)
    private Long playlistId;
}
