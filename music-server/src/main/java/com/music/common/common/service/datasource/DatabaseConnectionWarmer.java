package com.music.common.common.service.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库连接池预热配置
 * 解决首次连接超时问题
 */
@Slf4j
@Component
public class DatabaseConnectionWarmer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 应用启动时预热数据库连接池
     * 执行简单查询确保连接已建立
     */
    @PostConstruct
    public void warmUpConnectionPool() {
        try {
            log.info("预热数据库连接池...");
            jdbcTemplate.execute("SELECT 1");
            log.info("数据库连接池预热成功");
        } catch (Exception e) {
            log.error("数据库连接池预热失败", e);
        }
    }
}