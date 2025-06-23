package fe.be_inkrealm_demo2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Lớp Entity cho bảng 'segment'
 */
@Entity
@Table(name = "segment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID
    @Column(name = "segment_id")
    private Long segmentId;

    @ManyToOne(fetch = FetchType.LAZY) // Mối quan hệ nhiều-tới-một với bảng Chapter
    @JoinColumn(name = "chapter_id", nullable = false) // Cột khóa ngoại 'chapter_id'
    private Chapter chapter;

    @Column(name = "segment_text", nullable = false, columnDefinition = "TEXT") // Ánh xạ tới TEXT
    private String segmentText;

    @Column(name = "segment_number", nullable = false)
    private Integer segmentNumber;
}
