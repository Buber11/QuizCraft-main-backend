package main.QuizCraft.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

//RCF 7808
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Response<T> {

    private String status;
    private Integer code;
    private String title;
    private String detail;
    private String instance;
    private T view;

    public Response(String status,
                    Integer code,
                    String title,
                    String detail,
                    String instance) {
        this.status = status;
        this.code = code;
        this.title = title;
        this.detail = detail;
        this.instance = instance;
    }

    public Response(T view) {
        this.view = view;
    }

    public Response() {
    }

    public <T extends Response > T cast(Class<T> targetClass){
        return targetClass.cast(this);
    }

    public abstract Integer getCodeHttp();

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", instance='" + instance + '\'' +
                ", view=" + (view != null ? view.toString() : "null") +
                '}';
    }
}
