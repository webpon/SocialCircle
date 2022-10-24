package com.socialCircle.constant;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class RedisQuery <T>{
    private String prefixKey;
    private String suffixKey;
    private Date date;
    private T data;
    private Integer offset;

    public RedisQuery(String prefixKey, String suffixKey, T data, DateField dateField, Integer offset) {
        this.prefixKey = prefixKey;
        this.suffixKey = suffixKey;
        this.data = data;
        this.offset = offset;

        String now = DateUtil.now();
        DateTime parse = DateUtil.parse(now);
        this.date = DateUtil.offset(parse.toJdkDate(), dateField, offset);
    }

    public RedisQuery() {
    }
}
