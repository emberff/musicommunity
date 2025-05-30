package com.music.common.music.domain.vo.request;

import com.music.common.common.domain.vo.req.PageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 歌曲评论分页请求类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentPageReq extends PageBaseReq {

    @NotNull
    @ApiModelProperty(value = "歌曲id", required = true)
    private Long songId;
}
