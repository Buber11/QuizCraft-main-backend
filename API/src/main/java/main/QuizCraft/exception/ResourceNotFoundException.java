package main.QuizCraft.exception;

public class ResourceNotFoundException extends RuntimeException{

    private long id;
    private String instanceName;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message, long id, String instanceName) {
        super(message);
        this.id = id;
        this.instanceName = instanceName;
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public long getId() {
        return id;
    }

    public String getInstanceName() {
        return instanceName;
    }
}
