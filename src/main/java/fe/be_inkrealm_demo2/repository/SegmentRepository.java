package fe.be_inkrealm_demo2.repository;

import fe.be_inkrealm_demo2.entity.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Giao diện Repository cho bảng Segment
 * Cung cấp các phương thức CRUD cơ bản cho entity Segment
 */
@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {
    // Tìm tất cả các đoạn thuộc một chương cụ thể, sắp xếp theo segment_number
    List<Segment> findByChapter_ChapterIdOrderBySegmentNumberAsc(Long chapterId);

    // Tìm một đoạn cụ thể theo chapterId và segmentNumber
    Optional<Segment> findByChapter_ChapterIdAndSegmentNumber(Long chapterId, Integer segmentNumber);
}
