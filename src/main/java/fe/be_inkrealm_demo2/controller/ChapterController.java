package fe.be_inkrealm_demo2.controller;

import fe.be_inkrealm_demo2.dto.request.ChapterRequestDTO;
import fe.be_inkrealm_demo2.dto.response.ChapterResponseDTO;
import fe.be_inkrealm_demo2.service.ChapterService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
//import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "http://localhost:8080/api/chapters",
                        description = "Chapter Server")
        }
)
@RestController
@RequestMapping("/api/chapter")
@Tag(name = "Chapter Controller")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    /**
     * GET /api/chapters : Lấy tất cả các chương.
     * @return Danh sách các chương dưới dạng ChapterResponseDTO
     */
    @GetMapping
//    @Operation(summary = "hello")
    public List<ChapterResponseDTO> getAllChapters() {
        return chapterService.getAllChapters();
    }

    /**
     * GET /api/chapters/{id} : Lấy chương theo ID.
     * @param id ID của chương
     * @return ResponseEntity chứa chương nếu tìm thấy, hoặc NOT_FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChapterResponseDTO> getChapterById(
            @PathVariable Long id) {
        return chapterService.getChapterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/chapters/by-story/{storyId} : Lấy tất cả các chương thuộc một truyện.
     * @param storyId ID của truyện
     * @return Danh sách các chương dưới dạng ChapterResponseDTO
     */
    @GetMapping("/by-story/{storyId}")
    public List<ChapterResponseDTO> getChaptersByStoryId(
            @PathVariable Long storyId) {
        return chapterService.getChaptersByStoryId(storyId);
    }

    /**
     * POST /api/chapters : Tạo một chương mới.
     * @param chapterRequestDTO Đối tượng ChapterRequestDTO từ request body
     * @return ResponseEntity chứa chương đã tạo và HttpStatus.CREATED
     */
    @PostMapping
    public ResponseEntity<ChapterResponseDTO> createChapter(
            @RequestBody ChapterRequestDTO chapterRequestDTO) {
        try {
            ChapterResponseDTO createdChapter = chapterService.createChapter(chapterRequestDTO);
            return new ResponseEntity<>(createdChapter, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/chapters/{id} : Cập nhật thông tin một chương.
     * @param id ID của chương cần cập nhật
     * @param chapterRequestDTO Đối tượng ChapterRequestDTO chứa thông tin cập nhật
     * @return ResponseEntity chứa chương đã cập nhật nếu thành công, hoặc NOT_FOUND/BAD_REQUEST
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChapterResponseDTO> updateChapter(
            @PathVariable Long id,
            @RequestBody ChapterRequestDTO chapterRequestDTO) {
        try {
            return chapterService.updateChapter(id, chapterRequestDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/chapters/{id} : Xóa logic một chương (cập nhật trạng thái thành ARCHIVED).
     * @param id ID của chương cần xóa logic
     * @return ResponseEntity với HttpStatus.NO_CONTENT nếu xóa logic thành công, hoặc NOT_FOUND
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> logicallyDeleteChapter(@PathVariable Long id) {
        try {
            if (chapterService.logicallyDeleteChapter(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

