package fe.be_inkrealm_demo2.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SegmentRequestDTO {
    private Long chapterId; // ID của chương mà đoạn này thuộc về
    private String segmentText;
    private Integer segmentNumber;
}
