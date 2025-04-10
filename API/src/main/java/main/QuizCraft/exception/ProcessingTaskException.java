package main.QuizCraft.exception;

public class ProcessingTaskException extends RuntimeException {
    public ProcessingTaskException(String message) {
        super(message);
    }

    public ProcessingTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessingTaskException(Throwable cause) {
        super(cause);
    }
}
