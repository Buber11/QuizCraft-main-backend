package main.QuizCraft.exception;

public class AiResponseException extends RuntimeException {

    private String title;
    private String details;

    public AiResponseException(String theMessage, String theDescription) {
        title = theMessage;
        details = theDescription;
    }

    public AiResponseException(String message) {
        super(message);
    }

    public AiResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiResponseException(Throwable cause) {
        super(cause);
    }

    public AiResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }
}
