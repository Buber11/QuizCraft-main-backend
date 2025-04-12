package main.QuizCraft.service.document;

import jakarta.servlet.http.HttpServletRequest;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.kafka.ProcessingTask;
import org.springframework.web.multipart.MultipartFile;

public interface DocUploadService {

    ProcessingTaskStatusDto processText(HttpServletRequest request, String text);
    ProcessingTaskStatusDto processDoc(HttpServletRequest request, MultipartFile file);

}
