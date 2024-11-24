package main.QuizCraft.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Response {
    private static int DEFAULT_CODE = 200;

    private String status;
    private int code = DEFAULT_CODE;
    private String title;
    private String detail;
    private String instance;

    public Response(String status, int code, String title, String detail, String instance) {
        this.status = status;
        this.code = code;
        this.title = title;
        this.detail = detail;
        this.instance = instance;
    }

    public Response() {
    }

    public <T extends Response > T cast(Class<T> targetClass){
        return targetClass.cast(this);
    }
}
