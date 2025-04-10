package main.QuizCraft.handler;

import main.QuizCraft.exception.ProcessingTaskException;
import main.QuizCraft.response.FailureResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TaskHanlderException {
    @ExceptionHandler(value = ProcessingTaskException.class)
    public ResponseEntity<FailureResponse> handleProcessingTaskException(ProcessingTaskException e) {
        return ResponseEntity
                .status(404)
                .body( FailureResponse.builder()
                                .title("Task processing error")
                                .code(404)
                                .instance("task")
                                .status("http://QuizCraft/problems/task-processing")
                                .detail(e.getMessage())
                                .build()
                );
    }
}
