package fe.be_inkrealm_demo2.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Story API", // Tiêu đề của API
                version = "1.0", // Phiên bản API
                description = "REST API cho ứng dụng truyện chữ. Cung cấp các thao tác CRUD cho Story, Chapter, Segment và Status.", // Mô tả API
                contact = @Contact(
                        name = "Your Name", // Tên của bạn
                        email = "your.email@example.com" // Email của bạn
                ),
                license = @License(
                        name = "Apache 2.0", // Giấy phép
                        url = "[http://www.apache.org/licenses/LICENSE-2.0.html](http://www.apache.org/licenses/LICENSE-2.0.html)"
                )
        )
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi storyApi() {
        return GroupedOpenApi.builder()
                .group("Story Controller")
                .pathsToMatch("/api/story/**")
                .build();
    }

    @Bean
    public GroupedOpenApi chapterApi() {
        return GroupedOpenApi.builder()
                .group("Chapter Controller")
                .pathsToMatch("/api/chapter/**")
                .build();
    }

    @Bean
    public GroupedOpenApi statusApi() {
        return GroupedOpenApi.builder()
                .group("Status Controller")
                .pathsToMatch("/api/status/**")
                .build();
    }

    @Bean
    public GroupedOpenApi segmentApi() {
        return GroupedOpenApi.builder()
                .group("Segment Controller")
                .pathsToMatch("/api/segment/**")
                .build();
    }

    // Thêm các controller khác tương tự...
}

