package fe.be_inkrealm_demo2.service;

import fe.be_inkrealm_demo2.dto.request.StatusRequestDTO;
import fe.be_inkrealm_demo2.dto.response.StatusResponseDTO;
import fe.be_inkrealm_demo2.entity.Status;
import fe.be_inkrealm_demo2.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * Chuyển đổi Status entity thành StatusResponseDTO.
     */
    private StatusResponseDTO convertToDto(Status status) {
        if (status == null) return null;
        return new StatusResponseDTO(status.getStatusCode(), status.getStatusDescription());
    }

    /**
     * Chuyển đổi StatusRequestDTO thành Status entity.
     */
    private Status convertToEntity(StatusRequestDTO dto) {
        if (dto == null) return null;
        return new Status(dto.getStatusCode(), dto.getStatusDescription());
    }

    /**
     * Lấy tất cả các trạng thái.
     */
    public List<StatusResponseDTO> getAllStatuses() {
        return statusRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Lấy trạng thái theo mã.
     */
    public Optional<StatusResponseDTO> getStatusByCode(String statusCode) {
        return statusRepository.findById(statusCode)
                .map(this::convertToDto);
    }

    /**
     * Tạo hoặc cập nhật một trạng thái.
     */
    public StatusResponseDTO saveStatus(StatusRequestDTO statusRequestDTO) {
        Status status = convertToEntity(statusRequestDTO);
        Status savedStatus = statusRepository.save(status);
        return convertToDto(savedStatus);
    }

    /**
     * Cập nhật một trạng thái hiện có.
     */
    public Optional<StatusResponseDTO> updateStatus(String statusCode, StatusRequestDTO statusRequestDTO) {
        return statusRepository.findById(statusCode)
                .map(existingStatus -> {
                    existingStatus.setStatusDescription(statusRequestDTO.getStatusDescription());
                    Status updatedStatus = statusRepository.save(existingStatus);
                    return convertToDto(updatedStatus);
                });
    }

    /**
     * Xóa một trạng thái theo mã.
     */
    public void deleteStatus(String statusCode) {
        statusRepository.deleteById(statusCode);
    }
}

