package main.QuizCraft.service.document;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.exception.DocProcessingException;
import main.QuizCraft.kafka.MethodProcessingType;
import main.QuizCraft.kafka.TOPIC;
import main.QuizCraft.service.task.TaskManagerService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocUploadServiceImpl implements DocUploadService{

    private final TaskManagerService taskManagerService;

    @Override
    public ProcessingTaskStatusDto processText(HttpServletRequest request, String text) {
        return taskManagerService.createTask(
                new HashMap<>(Map.of("text", text)),
                request,
                TOPIC.TEXT_PROCESSING_REQUEST,
                MethodProcessingType.DOCUMENT_PROCESSING
        );
    }

    @Override
    public ProcessingTaskStatusDto processDoc(HttpServletRequest request, MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document);
            return processText(request, extractedText);
        } catch (IOException e) {
            throw  new DocProcessingException(e.getMessage());
        }
    }
}
