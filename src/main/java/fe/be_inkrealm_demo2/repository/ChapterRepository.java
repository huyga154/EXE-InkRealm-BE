package fe.be_inkrealm_demo2.repository;

import fe.be_inkrealm_demo2.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Giao diện Repository cho bảng Chapter
 * Cung cấp các phương thức CRUD cơ bản cho entity Chapter
 */
@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByStory_StoryIdOrderByChapterIndexAsc(Long storyId);
    Optional<Chapter> findByStory_StoryIdAndChapterIndex(
            Long storyId,
            BigDecimal chapterIndex);
}
