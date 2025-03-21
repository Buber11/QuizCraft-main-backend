package main.QuizCraft.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//RCF 7808
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FailureResponse {

    private String status;
    private Integer code;
    private String title;
    private String detail;
    private String instance;


}
