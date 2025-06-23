package fe.be_inkrealm_demo2.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SegmentResponseDTO implements Serializable {
    private Long segmentId;
    private Long chapterId; // ID của chương
    private String chapterName; // Tên chương (để hiển thị thông tin đầy đủ hơn)
    private String segmentText;
    private Integer segmentNumber;
}