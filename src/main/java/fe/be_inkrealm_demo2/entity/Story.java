package fe.be_inkrealm_demo2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lớp Entity cho bảng 'story'
 */
@Entity
@Table(name = "story")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID
    @Column(name = "story_id")
    private Long storyId;

    @Column(name = "story_name", nullable = false, length = 255)
    private String storyName;

    @Column(name = "story_img", length = 255) // Store the URL (max 255 characters typically)
    private String storyImg;

    @Column(name = "story_author", nullable = false, length = 255)
    private String storyAuthor;

    @Column(name = "story_converter", length = 255)
    private String storyConverter;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "story_description", columnDefinition = "TEXT")
    private String storyDescription;

    @Column(name = "last_time_update", nullable = false)
    private LocalDateTime lastTimeUpdate;

    @ManyToOne(fetch = FetchType.LAZY) // Mối quan hệ nhiều-tới-một với bảng Status
    @JoinColumn(name = "status_code", nullable = false) // Cột khóa ngoại 'status_code'
    private Status status;

    // Trước khi lưu một Story mới, thiết lập createdTime và lastTimeUpdate
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        lastTimeUpdate = LocalDateTime.now();
    }

    // Trước khi cập nhật một Story, cập nhật lastTimeUpdate
    @PreUpdate
    protected void onUpdate() {
        lastTimeUpdate = LocalDateTime.now();
    }
}
