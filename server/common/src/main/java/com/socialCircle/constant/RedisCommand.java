package com.socialCircle.constant;

@FunctionalInterface
public interface RedisCommand {
    /**
     * 异步查询
     */
    void run(String key);
}
