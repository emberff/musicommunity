package com.music.common.common.exception;

import cn.hutool.http.ContentType;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DENIED(401, "登录失效,请重新登录");


    private Integer httpCode;
    private String desc;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(httpCode, desc)));
    }

}
