package com.htruong48.common_log.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Chỉ dùng trên hàm
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceName {
    String value();
}
