package main.QuizCraft.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LlamaResponse extends Response{
    private String message;

    public LlamaResponse(String message,
                         String status,
                         int code,
                         String title,
                         String detail,
                         String instance) {
        super(status, code, title, detail, instance);
        this.message = message;
    }
    public LlamaResponse(){};
}