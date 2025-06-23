package fe.be_inkrealm_demo2.controller;

import fe.be_inkrealm_demo2.dto.request.StatusRequestDTO;
import fe.be_inkrealm_demo2.dto.response.StatusResponseDTO;
import fe.be_inkrealm_demo2.service.StatusService;
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
                        url = "http://localhost:8080/api/status",
                        description = "Status Server")
        }
)
@RestController
@RequestMapping("/api/status")
@Tag(name = "Status Controller")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    /**
     * GET /api/statuses : Lấy tất cả các trạng thái.
     * @return Danh sách các trạng thái dưới dạng StatusResponseDTO
     */
    @GetMapping
    public List<StatusResponseDTO> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    /**
     * GET /api/statuses/{statusCode} : Lấy trạng thái theo mã.
     * @param statusCode Mã trạng thái
     * @return ResponseEntity chứa trạng thái nếu tìm thấy, hoặc NOT_FOUND
     */
    @GetMapping("/{statusCode}")
    public ResponseEntity<StatusResponseDTO> getStatusByCode(@PathVariable String statusCode) {
        return statusService.getStatusByCode(statusCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/statuses : Tạo một trạng thái mới.
     * @param statusRequestDTO Đối tượng StatusRequestDTO từ request body
     * @return ResponseEntity chứa trạng thái đã tạo và HttpStatus.CREATED
     */
    @PostMapping
    public ResponseEntity<StatusResponseDTO> createStatus(@RequestBody StatusRequestDTO statusRequestDTO) {
        StatusResponseDTO createdStatus = statusService.saveStatus(statusRequestDTO);
        return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
    }

    /**
     * PUT /api/statuses/{statusCode} : Cập nhật một trạng thái hiện có.
     * @param statusCode Mã trạng thái cần cập nhật
     * @param statusRequestDTO Đối tượng StatusRequestDTO chứa thông tin cập nhật
     * @return ResponseEntity chứa trạng thái đã cập nhật nếu thành công, hoặc NOT_FOUND
     */
    @PutMapping("/{statusCode}")
    public ResponseEntity<StatusResponseDTO> updateStatus(@PathVariable String statusCode, @RequestBody StatusRequestDTO statusRequestDTO) {
        return statusService.updateStatus(statusCode, statusRequestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/statuses/{statusCode} : Xóa một trạng thái.
     * @param statusCode Mã trạng thái cần xóa
     * @return ResponseEntity với HttpStatus.NO_CONTENT nếu xóa thành công
     */
    @DeleteMapping("/{statusCode}")
    public ResponseEntity<Void> deleteStatus(@PathVariable String statusCode) {
        statusService.deleteStatus(statusCode);
        return ResponseEntity.noContent().build();
    }
}

