package com.music.common.music.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新建歌单请求体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongUpdateReq {

    @ApiModelProperty(value = "歌曲id, 使用jamendoApi时必填")
    @NotNull
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "歌曲名", required = true)
    private String name;

//    @NotNull
//    @ApiModelProperty(value = "歌曲类型, 0用户上传 1jamendoApi", required = true)
//    private Integer type;
    @ApiModelProperty(value = "歌手id")
    private Long singerId;

    @ApiModelProperty(value = "封面url", required = false)
    private String cover;

    @ApiModelProperty(value = "文件url", required = false)
    private String url;
}
