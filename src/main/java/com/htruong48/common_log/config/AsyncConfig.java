package com.htruong48.common_log.config;

import com.htruong48.common_log.utils.TraceUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNullApi;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("BotCrawl-");

        // Gắn Decorator để copy Trace ID
        executor.setTaskDecorator(new TraceTaskDecorator());

        executor.initialize();
        return executor;
    }

    // Class con để xử lý việc copy ID
    static class TraceTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            // Lấy ID từ thread cha
            String callerTraceId = TraceUtils.get();

            return () -> {
                try {
                    // Gán vào thread con
                    TraceUtils.set(callerTraceId);
                    runnable.run();
                } finally {
                    TraceUtils.clear();
                }
            };
        }
    }
}
