package com.htruong48.common_log.config;

import com.htruong48.common_log.utils.TraceUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter chặn mọi request để quản lý Trace ID (Correlation ID).
 * Luồng hoạt động:
 * 1. Request đến -> Kiểm tra Header (X-Trace-Id) xem có ID từ Service gọi đến không?
 * - Có: Lấy ID đó dùng lại (Distributed Tracing).
 * - Không: Tự sinh ra một ID mới.
 * 2. Lưu ID vào ThreadLocal (qua TraceUtils).
 * 3. Gán ID vào Response Header trả về cho client.
 * 4. Controller xử lý -> Log in ra kèm ID.
 * 5. Request xong (hoặc lỗi) -> Finally block chạy -> Xóa ID khỏi ThreadLocal (Dọn rác).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Chạy đầu tiên để đảm bảo có ID ngay lập tức
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Lấy Trace ID từ Header request (Do Feign Client hoặc Client gửi lên)
            // Lưu ý: StringConstant.TRACE_LOG phải khớp với key header bạn gửi (ví dụ: "X-Trace-Id")
            String traceId = request.getHeader(StringConstant.TRACE_LOG);

            if (traceId != null && !traceId.isEmpty()) {
                // 2a. Nếu có: Dùng lại ID cũ để nối mạch log
                TraceUtils.set(traceId);
            } else {
                // 2b. Nếu không: Tạo ID mới hoàn toàn
                TraceUtils.start();
            }

            // 3. Luôn trả lại Trace ID trong Response Header để Client tiện debug
            response.setHeader(StringConstant.TRACE_LOG, TraceUtils.get());

            // 4. Cho request đi tiếp vào Controller
            filterChain.doFilter(request, response);

        } finally {
            // 5. Quan trọng: Dọn dẹp ThreadLocal để tránh Memory Leak
            TraceUtils.clear();
        }
    }
}