package main.QuizCraft.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import main.QuizCraft.model.user.User;

public class AuthResponse extends Response<User>{

    public AuthResponse(String status,
                        Integer code,
                        String title,
                        String detail,
                        String instance) {
        super(status, code, title, detail, instance);
    }

    public AuthResponse() {
    }

    public AuthResponse(User view) {
        super(view);
    }

    @Override
    public Integer getCodeHttp() {
        return super.getCode() != null ? super.getCode() : 204;
    }

}
