package fe.be_inkrealm_demo2.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponseDTO implements Serializable {
    private String statusCode;
    private String statusDescription;
}
