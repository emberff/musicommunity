package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发帖分页返回体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发帖分页返回体")
public class PostPageResp {

    @ApiModelProperty("帖子id")
    private Long id;

    @ApiModelProperty("帖子描述")
    private String description;

    @ApiModelProperty("url")
    private String url;

}
