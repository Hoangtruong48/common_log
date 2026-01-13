package com.htruong48.common_log.aspect;


import com.htruong48.common_log.annotaion.TraceName;
import com.htruong48.common_log.utils.TraceUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1) // Chạy sau Filter nhưng trước các Aspect log khác
public class TraceNameAspect {

    @Around(value = "@annotation(traceName)", argNames = "joinPoint,traceName") // Chỉ áp dụng cho hàm có @TraceName
    public Object handleTraceName(ProceedingJoinPoint joinPoint, TraceName traceName) throws Throwable {

        String prefix = traceName.value();

        TraceUtils.appendPrefix(prefix);

        try {
            return joinPoint.proceed();
        } finally {

        }
    }
}
