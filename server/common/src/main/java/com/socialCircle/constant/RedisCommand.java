package com.socialCircle.constant;

@FunctionalInterface
public interface RedisCommand<T> {
    /**
     * 异步查询
     */
    T run(String key);
}
