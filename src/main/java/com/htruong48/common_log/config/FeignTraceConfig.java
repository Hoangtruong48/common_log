package com.htruong48.common_log.config;

import com.htruong48.common_log.utils.TraceUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j; // Thêm log để debug
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignTraceConfig {

    @Bean
    public RequestInterceptor traceIdInterceptor() {
        return template -> {
            String traceId = TraceUtils.get();
            if (traceId != null) {
                // Log ra để xem nó có chạy vào đây không
//                log.info(">> [FEIGN-INTERCEPTOR] Adding X-Trace-Id: {}", traceId);
                template.header(StringConstant.TRACE_LOG, traceId);
            }
        };
    }
}