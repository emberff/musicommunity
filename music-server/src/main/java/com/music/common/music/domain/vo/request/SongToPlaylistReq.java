package com.music.common.music.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 歌单添加/删除歌曲类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongToPlaylistReq {

    @NotNull
    @ApiModelProperty(value = "歌单id", required = true)
    private Long playlistId;

    @NotNull
    @ApiModelProperty(value = "歌曲id列表", required = true, example = "[1, 2]")
    private List<Long> songIds;

}
