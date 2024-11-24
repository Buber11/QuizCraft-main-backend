package main.QuizCraft.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

//RCF 7808
@Data
@Accessors(chain = true)
public abstract class Response {
    private static int DEFAULT_OK_CODE = 200;

    private String status;
    private int code = DEFAULT_OK_CODE;
    private String title;
    private String detail;
    private String instance;

    public Response(String status,
                    int code,
                    String title,
                    String detail,
                    String instance) {
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
