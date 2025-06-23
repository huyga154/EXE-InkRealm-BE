package fe.be_inkrealm_demo2.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterRequestDTO {
    private Long storyId; // ID của truyện mà chương này thuộc về
    private BigDecimal chapterIndex;
    private String chapterName;
    private Boolean draft;
    private String statusCode; // Mã trạng thái của chương
}
