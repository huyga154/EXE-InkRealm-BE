package fe.be_inkrealm_demo2.service;

import fe.be_inkrealm_demo2.dto.request.SegmentRequestDTO;
import fe.be_inkrealm_demo2.dto.response.SegmentResponseDTO;
import fe.be_inkrealm_demo2.entity.Chapter;
import fe.be_inkrealm_demo2.entity.Segment;
import fe.be_inkrealm_demo2.repository.ChapterRepository;
import fe.be_inkrealm_demo2.repository.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Lớp Service cho quản lý Segment.
 * Xử lý logic nghiệp vụ liên quan đến các đoạn văn bản và chuyển đổi giữa Entity và DTO.
 */
@Service
public class SegmentService {

    private final SegmentRepository segmentRepository;

    private final ChapterRepository chapterRepository;

    public SegmentService(SegmentRepository segmentRepository, ChapterRepository chapterRepository) {
        this.segmentRepository = segmentRepository;
        this.chapterRepository = chapterRepository;
    }

    /**
     * Chuyển đổi Segment entity thành SegmentResponseDTO.
     * @param segment Entity Segment
     * @return SegmentResponseDTO tương ứng
     */
    private SegmentResponseDTO convertToDto(Segment segment) {
        if (segment == null) return null;
        // Sử dụng constructor thay vì builder
        return new SegmentResponseDTO(
                segment.getSegmentId(),
                segment.getChapter() != null ? segment.getChapter().getChapterId() : null,
                segment.getChapter() != null ? segment.getChapter().getChapterName() : null,
                segment.getSegmentText(),
                segment.getSegmentNumber()
        );
    }

    /**
     * Lấy tất cả các đoạn.
     * @return Danh sách các đối tượng SegmentResponseDTO
     */
    public List<SegmentResponseDTO> getAllSegments() {
        return segmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Lấy đoạn theo ID.
     * @param id ID của đoạn
     * @return Optional chứa đối tượng SegmentResponseDTO nếu tìm thấy, ngược lại là rỗng
     */
    public Optional<SegmentResponseDTO> getSegmentById(Long id) {
        return segmentRepository.findById(id)
                .map(this::convertToDto);
    }

    /**
     * Lấy tất cả các đoạn thuộc một chương.
     * @param chapterId ID của chương
     * @return Danh sách các đối tượng SegmentResponseDTO
     */
    public List<SegmentResponseDTO> getSegmentsByChapterId(Long chapterId) {
        return segmentRepository.findByChapter_ChapterIdOrderBySegmentNumberAsc(chapterId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Tạo một đoạn mới từ SegmentRequestDTO.
     * @param segmentRequestDTO Đối tượng SegmentRequestDTO cần tạo
     * @return Đối tượng SegmentResponseDTO đã được tạo
     * @throws RuntimeException nếu chapter_id không tồn tại
     */
    @Transactional
    public SegmentResponseDTO createSegment(SegmentRequestDTO segmentRequestDTO) {
        // Tìm Chapter entity dựa trên ID từ DTO
        Chapter chapter = chapterRepository.findById(segmentRequestDTO.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter with ID " + segmentRequestDTO.getChapterId() + " not found."));

        Segment segment = new Segment();
        segment.setChapter(chapter);
        segment.setSegmentText(segmentRequestDTO.getSegmentText());
        segment.setSegmentNumber(segmentRequestDTO.getSegmentNumber());

        Segment createdSegment = segmentRepository.save(segment);
        return convertToDto(createdSegment);
    }

    /**
     * Cập nhật thông tin một đoạn từ SegmentRequestDTO.
     * @param id ID của đoạn cần cập nhật
     * @param segmentRequestDTO Đối tượng SegmentRequestDTO chứa thông tin cập nhật
     * @return Optional chứa đối tượng SegmentResponseDTO đã được cập nhật nếu thành công, ngược lại là rỗng
     * @throws RuntimeException nếu chapter_id không tồn tại
     */
    @Transactional
    public Optional<SegmentResponseDTO> updateSegment(Long id, SegmentRequestDTO segmentRequestDTO) {
        return segmentRepository.findById(id)
                .map(existingSegment -> {
                    existingSegment.setSegmentText(segmentRequestDTO.getSegmentText());
                    existingSegment.setSegmentNumber(segmentRequestDTO.getSegmentNumber());

                    // Cập nhật Chapter nếu có thay đổi (chỉ cho phép thay đổi chapterId nếu có, không tự động tạo)
                    if (segmentRequestDTO.getChapterId() != null &&
                            (existingSegment.getChapter() == null || !existingSegment.getChapter().getChapterId().equals(segmentRequestDTO.getChapterId()))) {
                        Chapter newChapter = chapterRepository.findById(segmentRequestDTO.getChapterId())
                                .orElseThrow(() -> new RuntimeException("Chapter with ID " + segmentRequestDTO.getChapterId() + " not found."));
                        existingSegment.setChapter(newChapter);
                    }
                    Segment updatedSegment = segmentRepository.save(existingSegment);
                    return convertToDto(updatedSegment);
                });
    }

    /**
     * Xóa vật lý một đoạn.
     * @param id ID của đoạn cần xóa
     */
    public void deleteSegment(Long id) {
        segmentRepository.deleteById(id);
    }
}
