package com.music.common.common.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ShortSnowflakeIdGenerator implements IdentifierGenerator {

    // 每秒最多支持 100 个 ID，可调
    private static final int MAX_SEQUENCE = 99;
    private AtomicInteger sequence = new AtomicInteger(0);
    private long lastSecond = -1;

    @Override
    public Long nextId(Object entity) {
        long currentSecond = Instant.now().getEpochSecond();

        synchronized (this) {
            if (currentSecond != lastSecond) {
                lastSecond = currentSecond;
                sequence.set(0);
            } else if (sequence.get() >= MAX_SEQUENCE) {
                // 等待下一秒
                while (currentSecond == lastSecond) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignored) {}
                    currentSecond = Instant.now().getEpochSecond();
                }
                lastSecond = currentSecond;
                sequence.set(0);
            }

            int seq = sequence.getAndIncrement();
            return currentSecond * 100 + seq; // 组合成一个 Long：秒 * 100 + 序列号
        }
    }
}
