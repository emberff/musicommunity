package com.music.common.common.domain.vo.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.music.common.common.exception.ErrorEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: 通用返回体
 * Author: <a href="https://github.com/emberff">emberff</a>
 * Date: 2023-03-23
 */
@Data
@ApiModel("基础返回体")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResult<T> {
    @ApiModelProperty("成功标识true or false")
    private Boolean success;
    @ApiModelProperty("错误码")
    private Integer errorCode;
    @ApiModelProperty("错误消息")
    private String errorMsg;
    @ApiModelProperty("返回对象")
    private T data;

    public static <T> ApiResult<T> success() {
        ApiResult<T> result = new ApiResult<T>();
        result.setData(null);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult<T>();
        result.setData(data);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> ApiResult<T> fail(Integer code, String msg) {
        ApiResult<T> result = new ApiResult<T>();
        result.setSuccess(Boolean.FALSE);
        result.setErrorCode(code);
        result.setErrorMsg(msg);
        return result;
    }

    public static <T> ApiResult<T> fail(ErrorEnum error) {
        ApiResult<T> result = new ApiResult<T>();
        result.setSuccess(Boolean.FALSE);
        result.setErrorCode(error.getErrorCode());
        result.setErrorMsg(error.getErrorMsg());
        return result;
    }

    public boolean isSuccess() {
        return this.success;
    }
}

