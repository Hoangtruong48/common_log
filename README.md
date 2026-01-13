# Common Log & Tracing Library

**Common Log** là thư viện dùng chung cho các Microservice Spring Boot, cung cấp giải pháp **Log tập trung** và **Truy vết phân tán (Distributed Tracing)**.

Thư viện giúp giải quyết vấn đề theo dõi luồng đi của một request khi nó di chuyển qua nhiều service khác nhau (Service A -> Service B -> Service C) thông qua một mã định danh duy nhất (`traceId`).

---

## ✨ Tính năng nổi bật

1. **Trace ID duy nhất:** Tự động sinh `traceId` cho mỗi request gửi đến.
2. **Lan truyền Trace ID:** Tự động gửi kèm `traceId` sang các service khác khi gọi qua Feign Client (Header: `X-Trace-Id`).
3. **Log tự động:** Tự động log thời gian Bắt đầu/Kết thúc, Tham số đầu vào và Thời gian xử lý của Controller.
4. **Hỗ trợ Async:** Đảm bảo `traceId` không bị mất khi xử lý đa luồng (`@Async`).
5. **Cấu hình tự động:** Chỉ cần thêm thư viện vào là chạy, không cần config phức tạp.

---

## 📦 Cài đặt

### 1. Build và cài đặt thư viện vào Maven local
Chạy lệnh sau tại thư mục gốc của project `common_log`:

```bash
mvn clean install
```

### 2. Thêm dependency vào file `pom.xml` của Service cần dùng

```xml
<dependency>
    <groupId>com.htruong48</groupId>
    <artifactId>common_log</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## ⚙️ Hướng dẫn sử dụng

### 1. Cấu hình hiển thị Log
Để nhìn thấy `traceId` trong console hoặc file log, bạn cần sửa pattern trong file `log4j2.xml` (hoặc `logback.xml`) thêm tham số `%X{traceId}`.

**Ví dụ:**

```xml
<Property name="pattern">%d{HH:mm:ss.SSS} [%X{traceId}] %-5level %logger{36} - %msg%n</Property>
```

### 2. Đặt tên nghiệp vụ cho Trace (Tùy chọn)
Sử dụng annotation `@TraceName` để dễ dàng tracking theo nghiệp vụ:

```java
@PostMapping("/create")
@TraceName("tao-don-hang") // TraceId sẽ thành: tao-don-hang-a1b2c3
public ApiResponse createOrder(...) {
    // ... logic xử lý
    return null;
}
```

---
## 🛠 Yêu cầu hệ thống

Hiện tại ở nhánh `main` hỗ trợ:
- **Java:** 17
- **Spring Boot:** 3.x (Spring Cloud 2023.x)
