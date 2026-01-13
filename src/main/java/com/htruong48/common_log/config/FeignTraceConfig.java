package com.htruong48.common_log.config;


import com.htruong48.common_log.utils.TraceUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignTraceConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Lấy ID từ túi của Service hiện tại
                String traceId = TraceUtils.get();

                // Nếu có, nhét vào Header gửi sang Service hàng xóm
                if (traceId != null) {
                    template.header("X-Trace-Id", traceId);
                }
            }
        };
    }
}
