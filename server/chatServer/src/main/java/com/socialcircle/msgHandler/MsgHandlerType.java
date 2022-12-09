package com.socialcircle.msgHandler;

import com.socialCircle.msg.Message;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MsgHandlerType {
    // 信息类型
    String value();
    // 信息类对象
    Class<? extends Message> clazz();
    // 是否保存
    boolean save() default true;
    // 排序
    int sort() default 1;
}
