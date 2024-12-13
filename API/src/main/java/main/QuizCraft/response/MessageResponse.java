package main.QuizCraft.response;

public class MessageResponse extends Response<String>{

    public MessageResponse(String status,
                           Integer code,
                           String title,
                           String detail,
                           String instance) {
        super(status, code, title, detail, instance);
    }

    public MessageResponse(String view) {
        super(view);
    }

    public MessageResponse(){}

    @Override
    public Integer getCodeHttp() {
        return super.getCode() != null ? super.getCode() : 200;
    }


}