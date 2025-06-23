package fe.be_inkrealm_demo2.controller;

import fe.be_inkrealm_demo2.dto.request.SegmentRequestDTO;
import fe.be_inkrealm_demo2.dto.response.SegmentResponseDTO;
import fe.be_inkrealm_demo2.service.SegmentService;
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
                        url = "http://localhost:8080/api/segments",
                        description = "Segment Server")
        }
)
@RestController
@RequestMapping("/api/segment")
@Tag(name = "Segment Controller")
public class SegmentController {

    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    /**
     * GET /api/segments : Lấy tất cả các đoạn.
     * @return Danh sách các đoạn dưới dạng SegmentResponseDTO
     */
    @GetMapping
    public List<SegmentResponseDTO> getAllSegments() {
        return segmentService.getAllSegments();
    }

    /**
     * GET /api/segments/{id} : Lấy đoạn theo ID.
     * @param id ID của đoạn
     * @return ResponseEntity chứa đoạn nếu tìm thấy, hoặc NOT_FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<SegmentResponseDTO> getSegmentById(
            @PathVariable Long id) {
        return segmentService.getSegmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/segments/by-chapter/{chapterId} : Lấy tất cả các đoạn thuộc một chương.
     * @param chapterId ID của chương
     * @return Danh sách các đoạn dưới dạng SegmentResponseDTO
     */
    @GetMapping("/by-chapter/{chapterId}")
    public List<SegmentResponseDTO> getSegmentsByChapterId(
            @PathVariable Long chapterId) {
        return segmentService.getSegmentsByChapterId(chapterId);
    }

    /**
     * POST /api/segments : Tạo một đoạn mới.
     * @param segmentRequestDTO Đối tượng SegmentRequestDTO từ request body
     * @return ResponseEntity chứa đoạn đã tạo và HttpStatus.CREATED
     */
    @PostMapping
    public ResponseEntity<SegmentResponseDTO> createSegment(
            @RequestBody SegmentRequestDTO segmentRequestDTO) {
        try {
            SegmentResponseDTO createdSegment = segmentService.createSegment(segmentRequestDTO);
            return new ResponseEntity<>(createdSegment, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/segments/{id} : Cập nhật thông tin một đoạn.
     * @param id ID của đoạn cần cập nhật
     * @param segmentRequestDTO Đối tượng SegmentRequestDTO chứa thông tin cập nhật
     * @return ResponseEntity chứa đoạn đã cập nhật nếu thành công, hoặc NOT_FOUND/BAD_REQUEST
     */
    @PutMapping("/{id}")
    public ResponseEntity<SegmentResponseDTO> updateSegment(@PathVariable Long id, @RequestBody SegmentRequestDTO segmentRequestDTO) {
        try {
            return segmentService.updateSegment(id, segmentRequestDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/segments/{id} : Xóa vật lý một đoạn.
     * @param id ID của đoạn cần xóa
     * @return ResponseEntity với HttpStatus.NO_CONTENT nếu xóa thành công
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSegment(@PathVariable Long id) {
        segmentService.deleteSegment(id);
        return ResponseEntity.noContent().build();
    }
}

