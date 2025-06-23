package fe.be_inkrealm_demo2.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryRequestDTO {
    private String storyName;
    private String storyImg;
    private String storyAuthor;
    private String storyConverter;
    private String storyDescription;
    private String statusCode; // Chỉ cần mã trạng thái để liên kết
}
