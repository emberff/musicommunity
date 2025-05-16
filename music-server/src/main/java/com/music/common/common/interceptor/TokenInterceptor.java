package com.music.common.common.interceptor;

import com.music.common.common.exception.HttpErrorEnum;
import com.music.common.user.dao.UserDao;
import com.music.common.user.service.IUserService;
import com.music.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_SCHEMA = "Bearer ";
    public static final String UID = "uid";

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // 直接放行，返回true让Spring的CORS机制自动加响应头
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) { // 用户有登录态
            request.setAttribute(UID, validUid);
        } else { // 用户未登录
            boolean isPublicURI = isPublicURI(request);
            if (!isPublicURI) {
                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }
        }
        return true;
    }


    private static boolean isPublicURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        return split.length > 2 && "public".equals(split[3]);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(AUTHORIZATION_SCHEMA))
                .map(h -> h.replaceFirst(AUTHORIZATION_SCHEMA, ""))
                .orElse(null);
    }
}
