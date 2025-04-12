package main.QuizCraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.service.document.DocUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/v1/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocUploadService docUploadService;

    @PostMapping("/text")
    public ResponseEntity<ProcessingTaskStatusDto> saveText(HttpServletRequest request,
                                                            @RequestBody() String text) {

        ProcessingTaskStatusDto taskStatusDto = docUploadService.processText(request, text);
        return ResponseEntity.accepted().body(taskStatusDto);
    }

    @PostMapping("/pdf")
    public ResponseEntity<ProcessingTaskStatusDto> savePDF(HttpServletRequest request,
                                                           @RequestParam("file") MultipartFile file) {
        ProcessingTaskStatusDto taskStatusDto = docUploadService.processDoc(request, file);
        return ResponseEntity.accepted().body(taskStatusDto);
    }
}



