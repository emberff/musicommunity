package com.music.common.common.config;

import com.music.common.common.interceptor.CollectorInterceptor;
import com.music.common.common.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Autowired
    private CollectorInterceptor collectorInterceptor;
//    @Autowired
//    private BlackInterceptor blackInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //先解析再收集信息
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/capi/**");
        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns("/capi/**");
//        registry.addInterceptor(blackInterceptor)
//                .addPathPatterns("/capi/**");
    }

    /**
     * 添加跨域配置，解决前端 CORS 报错
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有请求路径都支持跨域
                .allowedOriginPatterns("*") // 允许所有源，包括不同端口
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // 允许所有请求头
                .allowCredentials(true) // 允许携带 cookie 等凭证
                .maxAge(3600); // 预检请求缓存时间
    }



}
