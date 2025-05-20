package com.music.common.music.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 新增发布
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateReq {

    @ApiModelProperty(value = "帖子id")
    private Long id;

    @ApiModelProperty(value = "简述")
    private String description;

    @ApiModelProperty(value = "状态 0隐藏 1开启")
    private Integer status;

    @ApiModelProperty(value = "文件url")
    private List<String> urls;
}
