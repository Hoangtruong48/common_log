package com.htruong48.common_log.utils;

import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

public class TraceUtils {
    public static final String TRACE_KEY = "traceId";

    // Tạo traceId mới
    public static void start() {
        String id = UUID.randomUUID().toString().substring(0, 6);
        ThreadContext.put(TRACE_KEY, id);
    }

    // Gán một traceId cụ thể (dùng cho Async)
    public static void set(String id) {
        if (id != null) {
            ThreadContext.put(TRACE_KEY, id);
        }
    }

    // Lấy traceId hiện tại
    public static String get() {
        return ThreadContext.get(TRACE_KEY);
    }

    // Xóa traceId
    public static void clear() {
        ThreadContext.remove(TRACE_KEY);
        ThreadContext.clearAll();
    }

    // THÊM HÀM NÀY: Để gắn prefix vào ID cũ
    public static void appendPrefix(String prefix) {
        String currentId = ThreadContext.get(TRACE_KEY);
        // Nếu chưa có ID thì tạo mới, nếu có rồi thì nối thêm
        if (currentId == null) {
            currentId = UUID.randomUUID().toString().substring(0, 6);
        }

        // Kết quả: "tao-nguon-a1b2c3"
        String newId = prefix + "-" + currentId;
        ThreadContext.put(TRACE_KEY, newId);
    }
}
