package com.htruong48.common_log.config;

import com.htruong48.common_log.utils.TraceUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter chặn mọi request để gán Trace ID (Correlation ID).
 * Luồng hoạt động:
 * 1. Request đến -> Tạo ID -> Lưu vào ThreadLocal.
 * 2. Controller xử lý -> Log in ra kèm ID.
 * 3. Request xong (hoặc lỗi) -> Finally block chạy -> Xóa ID khỏi ThreadLocal.
 * Mục đích: Giúp truy vết (trace log) toàn bộ vòng đời của 1 request cụ thể,
 * tránh việc log bị lẫn lộn trong môi trường đa luồng.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Chạy đầu tiên
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            TraceUtils.start();

            response.setHeader(StringConstant.TRACE_LOG, TraceUtils.get());

            filterChain.doFilter(request, response);
        } finally {
            TraceUtils.clear();
        }
    }
}
