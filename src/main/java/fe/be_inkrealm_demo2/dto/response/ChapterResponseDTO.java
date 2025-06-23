package fe.be_inkrealm_demo2.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterResponseDTO {
    private Long chapterId;
    private Long storyId; // ID của truyện
    private String storyName; // Tên truyện (để hiển thị thông tin đầy đủ hơn)
    private BigDecimal chapterIndex;
    private String chapterName;
    private Boolean draft;
    private String createdTime;
    private String lastTimeUpdate;
    private String statusCode;
    private String statusDescription;


}
