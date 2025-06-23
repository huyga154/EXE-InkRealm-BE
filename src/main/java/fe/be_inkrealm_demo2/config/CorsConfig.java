package fe.be_inkrealm_demo2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình CORS (Cross-Origin Resource Sharing) cho ứng dụng Spring Boot.
 * Cho phép các yêu cầu từ frontend (client) được phép truy cập tài nguyên backend.
 */
@Configuration // Đánh dấu đây là một lớp cấu hình của Spring
@EnableWebMvc // Kích hoạt các cấu hình MVC được tùy chỉnh
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Cấu hình các quy tắc CORS.
     * @param registry Đối tượng CorsRegistry để đăng ký các cấu hình CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Áp dụng CORS cho tất cả các endpoint dưới /api/
                // THÊM CỔNG CỦA FRONTEND VÀO ĐÂY: http://localhost:5174
                // "http://localhost:8080" nếu bạn phục vụ HTML từ Spring Boot hoặc mở trực tiếp file HTML qua cổng của web server.
                // "null" thường xuất hiện khi bạn mở một file HTML cục bộ trực tiếp từ trình duyệt (file://).
                .allowedOrigins("http://localhost:5173", "https://exe-ink-realm.vercel.app", "http://localhost:8080", "null")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các phương thức HTTP này
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(true) // Cho phép gửi cookies, authorization headers, v.v.
                .maxAge(3600); // Thời gian mà kết quả pre-flight request có thể được cache (giây)
    }
}
