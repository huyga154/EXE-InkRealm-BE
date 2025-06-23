package fe.be_inkrealm_demo2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Lớp Entity cho bảng 'chapter'
 */
@Entity
@Table(name = "chapter")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID
    @Column(name = "chapter_id")
    private Long chapterId;

    @ManyToOne(fetch = FetchType.LAZY) // Mối quan hệ nhiều-tới-một với bảng Story
    @JoinColumn(name = "story_id", nullable = false) // Cột khóa ngoại 'story_id'
    private Story story;

    @Column(name = "chapter_index", nullable = false, precision = 10, scale = 2) // Ánh xạ tới NUMERIC(10, 2)
    private BigDecimal chapterIndex;

    @Column(name = "chapter_name", nullable = false, length = 255)
    private String chapterName;

    @Column(name = "draft", nullable = false)
    private Boolean draft = true;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "last_time_update", nullable = false)
    private LocalDateTime lastTimeUpdate;

    @ManyToOne(fetch = FetchType.LAZY) // Mối quan hệ nhiều-tới-một với bảng Status
    @JoinColumn(name = "status_code", nullable = false) // Cột khóa ngoại 'status_code'
    private Status status;

    // Trước khi lưu một Chapter mới, thiết lập createdTime và lastTimeUpdate
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        lastTimeUpdate = LocalDateTime.now();
    }

    // Trước khi cập nhật một Chapter, cập nhật lastTimeUpdate
    @PreUpdate
    protected void onUpdate() {
        lastTimeUpdate = LocalDateTime.now();
    }
}
