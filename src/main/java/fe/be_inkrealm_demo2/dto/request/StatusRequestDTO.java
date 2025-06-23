package fe.be_inkrealm_demo2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusRequestDTO {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("status_description")
    private String statusDescription;
}
