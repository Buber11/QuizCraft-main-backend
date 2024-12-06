package main.QuizCraft.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

//RCF 7808
@Data
@Accessors(chain = true)
public abstract class Response {

    private String status;
    private Integer code;
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

    public Integer getCode(){
        if(code != null){
            return code;
        }else {
            return 200;
        }
    }

}
