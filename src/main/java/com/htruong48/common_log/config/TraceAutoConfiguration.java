package com.htruong48.common_log.config;

import com.htruong48.common_log.aspect.ControllerLogAspect;
import com.htruong48.common_log.aspect.TraceNameAspect;
import com.htruong48.common_log.config.TraceFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
public class TraceAutoConfiguration {

    @Bean
    @ConditionalOnWebApplication
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

    @Bean
    public ControllerLogAspect controllerLogAspect() {
        return new ControllerLogAspect();
    }

    @Bean
    public TraceNameAspect traceNameAspect() {
        return new TraceNameAspect();
    }

    // ---> THÊM ĐOẠN NÀY VÀO CUỐI CLASS
    // Mục đích: Chỉ khi nào tìm thấy class "feign.RequestInterceptor" thì mới Import config kia
    @Configuration
    @ConditionalOnClass(name = "feign.RequestInterceptor")
    @Import(FeignTraceConfig.class)
    static class FeignConfiguration {
    }
}