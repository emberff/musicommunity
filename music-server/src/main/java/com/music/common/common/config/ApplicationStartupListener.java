package com.music.common.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ApplicationStartupListener {

    private final Environment environment;

    public ApplicationStartupListener(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // 延迟 2 秒，确保 HikariCP、Tomcat 等组件初始化完成
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            String port = environment.getProperty("server.port", "8080");
            String contextPath = environment.getProperty("server.servlet.context-path", "");

            log.info("✅ Spring Boot 应用已启动完成！");
            log.info("📌 API 文档地址: http://localhost:{}{}/doc.html", port, contextPath);
        }, 2, TimeUnit.SECONDS);
    }
}
