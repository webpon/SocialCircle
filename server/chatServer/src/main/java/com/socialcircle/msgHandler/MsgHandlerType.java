package com.socialcircle.msgHandler;

import com.socialCircle.entity.Message;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MsgHandlerType {
    String value();
    Class<? extends Message> clazz();
    boolean save() default true;
}
