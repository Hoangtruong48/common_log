package com.htruong48.common_log.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect // Đánh dấu đây là một Aspect
@Log4j2
public class ControllerLogAspect {

    // 1. Định nghĩa phạm vi: Áp dụng cho tất cả các class có @RestController
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerBean() {}

    // 2. Logic xử lý bao quanh (Around) hàm Controller
    @Around("controllerBean()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // Lấy tên class và tên hàm đang chạy
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        // Lấy thông tin Request (URL, Method, IP...)
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        String httpMethod = request.getMethod();

        // LOG START: In ra tham số đầu vào
        // Arrays.toString để in mảng object cho dễ đọc
        log.info("--> [REQ_START] {} {} | Class: {}.{} | Args: {}",
                httpMethod, url, className, methodName, Arrays.toString(joinPoint.getArgs()));

        Object result;
        try {
            // Cho phép hàm Controller thực thi
            result = joinPoint.proceed();
        } catch (Throwable e) {
            // LOG ERROR: Nếu code bị lỗi
            long timeTaken = System.currentTimeMillis() - start;
            log.error("---> [REQ_ERROR] {} {} | Time: {}ms | Error: {}",
                    httpMethod, url, timeTaken, e.getMessage());
            throw e; // Ném lỗi ra để GlobalExceptionHandler xử lý tiếp, không nuốt lỗi
        }

        // LOG END: In ra thời gian xử lý
        long timeTaken = System.currentTimeMillis() - start;
        log.info("---> [REQ_END] {} {} | Time: {}ms", httpMethod, url, timeTaken);

        return result;
    }
}
