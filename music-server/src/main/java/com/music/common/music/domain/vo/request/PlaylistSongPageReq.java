package com.music.common.music.domain.vo.request;

import com.music.common.common.domain.vo.req.PageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
/**
 * 歌单内歌曲分页请求类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSongPageReq extends PageBaseReq {

    @NotNull
    @ApiModelProperty(value = "歌单id", required = true)
    private Long playlistId;
}
