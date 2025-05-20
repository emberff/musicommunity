package com.music.common.music.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新增发布
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostAddReq {

    @NotBlank
    @ApiModelProperty(value = "简述", required = true)
    private String description;

    @ApiModelProperty(value = "文件url", required = true)
    private List<String> urls;
}
