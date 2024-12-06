package main.QuizCraft.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LlamaResponse extends Response<String>{

    public LlamaResponse(String status,
                         Integer code,
                         String title,
                         String detail,
                         String instance) {
        super(status, code, title, detail, instance);
    }

    public LlamaResponse(String view) {
        super(view);
    }

    public LlamaResponse(){}

    @Override
    public Integer getCodeHttp() {
        return super.getCode() != null ? super.getCode() : 200;
    }


}