package com.music.common.common.utils;


import com.music.common.common.domain.dto.RequestInfo;

/**
 * 请求上下文
 */
public class RequestHolder {

    private static final ThreadLocal<RequestInfo> threadlocal = new ThreadLocal<RequestInfo>();
    public static void set(RequestInfo requestInfo) {
        threadlocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadlocal.get();
    }

    public static void remove() {
        threadlocal.remove();
    }
}
