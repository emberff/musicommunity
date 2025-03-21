package com.music.common.music.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 歌单添加歌曲类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSongToPlaylistReq {

    @NotNull
    @ApiModelProperty("歌单id")
    private Long playlistId;

    @NotNull
    @ApiModelProperty("歌曲id列表")
    private List<Long> songIds;

}
