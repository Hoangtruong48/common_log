package com.htruong48.common_log.config;

import com.htruong48.common_log.utils.TraceUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TraceAspect {

    @Around("@annotation(com.htruong48.common_log.annotaion.AutoTrace)")
    public Object handleTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean isNewTrace = false;

        // Chỉ tạo mới nếu chưa có (để tránh đè lên ID của Web Filter nếu gọi lồng nhau)
        if (TraceUtils.get() == null) {
            TraceUtils.start();
            isNewTrace = true;
        }

        try {
            return joinPoint.proceed();
        } finally {
            // Chỉ xóa nếu chính Aspect này là người tạo ra
            if (isNewTrace) {
                TraceUtils.clear();
            }
        }
    }
}
