package fe.be_inkrealm_demo2.controller;

import fe.be_inkrealm_demo2.dto.request.StoryRequestDTO;
import fe.be_inkrealm_demo2.dto.response.StoryResponseDTO;
import fe.be_inkrealm_demo2.service.StoryService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "http://localhost:8080/api/stories",
                        description = "Story Server")
        }
)
@RestController
@RequestMapping("/api/story")
@Tag(name = "Story Controller")
public class StoryController {

    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    /**
     * GET /api/stories : Lấy tất cả các câu truyện.
     * @return Danh sách các câu truyện dưới dạng StoryResponseDTO
     */
    @GetMapping
    public List<StoryResponseDTO> getAllStories() {
        return storyService.getAllStories();
    }

    /**
     * GET /api/stories/{id} : Lấy câu truyện theo ID.
     * @param id ID của câu truyện
     * @return ResponseEntity chứa câu truyện nếu tìm thấy, hoặc NOT_FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<StoryResponseDTO> getStoryById(
            @PathVariable Long id) {
        return storyService.getStoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/stories : Tạo một câu truyện mới.
     * @param storyRequestDTO Đối tượng StoryRequestDTO từ request body
     * @return ResponseEntity chứa câu truyện đã tạo và HttpStatus.CREATED
     */
    @PostMapping
    public ResponseEntity<StoryResponseDTO> createStory(
            @RequestBody StoryRequestDTO storyRequestDTO) {
        try {
            StoryResponseDTO createdStory = storyService.createStory(storyRequestDTO);
            return new ResponseEntity<>(createdStory, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Trong thực tế, bạn nên trả về một đối tượng lỗi chuẩn và mã lỗi HTTP phù hợp hơn.
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/stories/{id} : Cập nhật thông tin một câu truyện.
     * @param id ID của câu truyện cần cập nhật
     * @param storyRequestDTO Đối tượng StoryRequestDTO chứa thông tin cập nhật
     * @return ResponseEntity chứa câu truyện đã cập nhật nếu thành công, hoặc NOT_FOUND/BAD_REQUEST
     */
    @PutMapping("/{id}")
    public ResponseEntity<StoryResponseDTO> updateStory(
            @PathVariable Long id,
            @RequestBody StoryRequestDTO storyRequestDTO) {
        try {
            return storyService.updateStory(id, storyRequestDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/stories/{id} : Xóa logic một câu truyện (cập nhật trạng thái thành ARCHIVED).
     * @param id ID của câu truyện cần xóa logic
     * @return ResponseEntity với HttpStatus.NO_CONTENT nếu xóa logic thành công, hoặc NOT_FOUND
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> logicallyDeleteStory(@PathVariable Long id) {
        try {
            if (storyService.logicallyDeleteStory(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Xử lý lỗi nếu trạng thái ARCHIVED không tồn tại
        }
    }
}

