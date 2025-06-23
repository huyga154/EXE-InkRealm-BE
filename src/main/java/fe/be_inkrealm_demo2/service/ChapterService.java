package fe.be_inkrealm_demo2.service;

import fe.be_inkrealm_demo2.dto.request.ChapterRequestDTO;
import fe.be_inkrealm_demo2.dto.response.ChapterResponseDTO;
import fe.be_inkrealm_demo2.entity.Chapter;
import fe.be_inkrealm_demo2.entity.Status;
import fe.be_inkrealm_demo2.entity.Story;
import fe.be_inkrealm_demo2.repository.ChapterRepository;
import fe.be_inkrealm_demo2.repository.StatusRepository;
import fe.be_inkrealm_demo2.repository.StoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final StoryRepository storyRepository;

    private final StatusRepository statusRepository;

    public ChapterService(ChapterRepository chapterRepository, StoryRepository storyRepository, StatusRepository statusRepository) {
        this.chapterRepository = chapterRepository;
        this.storyRepository = storyRepository;
        this.statusRepository = statusRepository;
    }

    /**
     * Chuyển đổi Chapter entity thành ChapterResponseDTO.
     * @param chapter Entity Chapter
     * @return ChapterResponseDTO tương ứng
     */
    private ChapterResponseDTO convertToDto(Chapter chapter) {
        ChapterResponseDTO output = new ChapterResponseDTO();
        if (chapter == null) return null;
        else {
            output.setChapterId(chapter.getChapterId());
            output.setChapterName(chapter.getChapterName());
            output.setChapterIndex(chapter.getChapterIndex());
            output.setChapterName(chapter.getChapterName());
            output.setDraft(chapter.getDraft());
            output.setCreatedTime(String.valueOf(chapter.getCreatedTime()));
            output.setLastTimeUpdate(String.valueOf(chapter.getLastTimeUpdate()));
        }
        return output;
    }

    /**
     * Lấy tất cả các chương.
     * @return Danh sách các đối tượng ChapterResponseDTO
     */
    public List<ChapterResponseDTO> getAllChapters() {
        return chapterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Lấy chương theo ID.
     * @param id ID của chương
     * @return Optional chứa đối tượng ChapterResponseDTO nếu tìm thấy, ngược lại là rỗng
     */
    public Optional<ChapterResponseDTO> getChapterById(Long id) {
        return chapterRepository.findById(id)
                .map(this::convertToDto);
    }

    /**
     * Lấy tất cả các chương thuộc một truyện.
     * @param storyId ID của truyện
     * @return Danh sách các đối tượng ChapterResponseDTO
     */
    public List<ChapterResponseDTO> getChaptersByStoryId(Long storyId) {
        return chapterRepository.findByStory_StoryIdOrderByChapterIndexAsc(storyId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Tạo một chương mới từ ChapterRequestDTO.
     * @param chapterRequestDTO Đối tượng ChapterRequestDTO cần tạo
     * @return Đối tượng ChapterResponseDTO đã được tạo
     * @throws RuntimeException nếu story_id hoặc status_code không tồn tại
     */
    @Transactional
    public ChapterResponseDTO createChapter(ChapterRequestDTO chapterRequestDTO) {
        // Tìm Story và Status entity dựa trên ID/code từ DTO
        Story story = storyRepository.findById(chapterRequestDTO.getStoryId())
                .orElseThrow(() -> new RuntimeException("Story with ID " + chapterRequestDTO.getStoryId() + " not found."));

        Status status = statusRepository.findById(chapterRequestDTO.getStatusCode())
                .orElseThrow(() -> new RuntimeException("Status with code " + chapterRequestDTO.getStatusCode() + " not found."));

        Chapter chapter = new Chapter();
        chapter.setStory(story);
        chapter.setChapterIndex(chapterRequestDTO.getChapterIndex());
        chapter.setChapterName(chapterRequestDTO.getChapterName());
        chapter.setDraft(chapterRequestDTO.getDraft());
        chapter.setStatus(status);

        Chapter createdChapter = chapterRepository.save(chapter);
        return convertToDto(createdChapter);
    }

    /**
     * Cập nhật thông tin một chương từ ChapterRequestDTO.
     * @param id ID của chương cần cập nhật
     * @param chapterRequestDTO Đối tượng ChapterRequestDTO chứa thông tin cập nhật
     * @return Optional chứa đối tượng ChapterResponseDTO đã được cập nhật nếu thành công, ngược lại là rỗng
     * @throws RuntimeException nếu story_id hoặc status_code không tồn tại
     */
    @Transactional
    public Optional<ChapterResponseDTO> updateChapter(Long id, ChapterRequestDTO chapterRequestDTO) {
        return chapterRepository.findById(id)
                .map(existingChapter -> {
                    existingChapter.setChapterIndex(chapterRequestDTO.getChapterIndex());
                    existingChapter.setChapterName(chapterRequestDTO.getChapterName());
                    existingChapter.setDraft(chapterRequestDTO.getDraft());

                    // Cập nhật Story nếu có thay đổi (chỉ cho phép thay đổi storyId nếu có, không tự động tạo)
                    if (chapterRequestDTO.getStoryId() != null &&
                            (existingChapter.getStory() == null || !existingChapter.getStory().getStoryId().equals(chapterRequestDTO.getStoryId()))) {
                        Story newStory = storyRepository.findById(chapterRequestDTO.getStoryId())
                                .orElseThrow(() -> new RuntimeException("Story with ID " + chapterRequestDTO.getStoryId() + " not found."));
                        existingChapter.setStory(newStory);
                    }

                    // Cập nhật Status nếu có thay đổi
                    if (chapterRequestDTO.getStatusCode() != null &&
                            (existingChapter.getStatus() == null || !existingChapter.getStatus().getStatusCode().equals(chapterRequestDTO.getStatusCode()))) {
                        Status newStatus = statusRepository.findById(chapterRequestDTO.getStatusCode())
                                .orElseThrow(() -> new RuntimeException("Status with code " + chapterRequestDTO.getStatusCode() + " not found."));
                        existingChapter.setStatus(newStatus);
                    }
                    Chapter updatedChapter = chapterRepository.save(existingChapter);
                    return convertToDto(updatedChapter);
                });
    }

    /**
     * Xóa logic một chương bằng cách thay đổi status_code thành 'ARCHIVED'.
     * @param id ID của chương cần xóa logic
     * @return true nếu xóa logic thành công, false nếu không tìm thấy chương hoặc trạng thái 'ARCHIVED' không tồn tại
     */
    @Transactional
    public boolean logicallyDeleteChapter(Long id) {
        return chapterRepository.findById(id)
                .map(chapter -> {
                    Optional<Status> archivedStatus = statusRepository.findById("ARCHIVED");
                    if (archivedStatus.isPresent()) {
                        chapter.setStatus(archivedStatus.get());
                        chapterRepository.save(chapter);
                        return true;
                    } else {
                        throw new RuntimeException("Status 'ARCHIVED' not found. Please ensure it's in the status table.");
                    }
                }).orElse(false);
    }
}


