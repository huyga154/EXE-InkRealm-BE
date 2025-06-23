package fe.be_inkrealm_demo2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryResponseDTO implements Serializable {
//    @JsonProperty("story_id")
    private Long storyId;
    private String storyName;
    private String storyImg;
    private String storyAuthor;
    private String storyConverter;
//    @JsonProperty("story_description")
    private String storyDescription;
    private String createdTime;
    private String lastTimeUpdate;
    private String statusCode;
    private String statusDescription;
}
