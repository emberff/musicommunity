package com.music.common.music.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新建歌单请求体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongAddReq {

    @ApiModelProperty(value = "歌曲id, 使用jamendoApi时必填")
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "歌曲名", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(value = "歌曲类型, 0用户上传 1jamendoApi", required = true)
    private Integer type;

    @NotBlank
    @ApiModelProperty(value = "封面url")
    private String cover;

    @ApiModelProperty(value = "文件url", required = true)
    private String url;
}
