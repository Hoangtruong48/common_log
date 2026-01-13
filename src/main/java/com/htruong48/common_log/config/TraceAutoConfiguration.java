package com.htruong48.common_log.config;


import com.htruong48.common_log.aspect.ControllerLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FeignTraceConfig.class)
public class TraceAutoConfiguration {

    // 1. Tự động đăng ký Filter
    @Bean
    @ConditionalOnWebApplication // Chỉ chạy nếu là Web App
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

    // 2. Tự động đăng ký Aspect log Controller
    @Bean
    public ControllerLogAspect controllerLogAspect() {
        return new ControllerLogAspect();
    }

    // 3. Tự động đăng ký Feign Interceptor (Nếu service có dùng Feign)
    // @Bean
    // @ConditionalOnClass(feign.RequestInterceptor.class)
    // public RequestInterceptor traceFeignInterceptor() { ... }
}
