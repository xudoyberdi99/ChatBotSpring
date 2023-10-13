package com.example.chatbotspring.controller;



import com.example.chatbotspring.entity.AttachmentEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.service.AttachmentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(value = "*",maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;


    @PostMapping("/addAttachment")
    public ResponseEntity<?> addAttach(MultipartHttpServletRequest request) throws IOException {
        ApiResponse apiResponse= attachmentService.addAttachment(request);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);

    }

    @GetMapping("/getAllAttachment")
    public ResponseEntity<Page<AttachmentEntity>> findAllFilesWithPageable(int page, int size){
        Page<AttachmentEntity> attachments=attachmentService.findAllWithPage(page, size);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> findAllFiles(){
        List<AttachmentEntity> attachments=attachmentService.findAllFiles();
        return ResponseEntity.ok(attachments);
    }
    @GetMapping("/user/getAttachment/{id}")
    public  ResponseEntity<?> downloadToServer(@PathVariable Long id, HttpServletResponse response) throws IOException {
        AttachmentEntity attachment= attachmentService.downloadToServer(id, response);
        return ResponseEntity.ok(attachment);
    }
    @GetMapping("/user/view/{id}")
    public  ResponseEntity<InputStreamResource> viewFile(@PathVariable Long id, HttpServletResponse httpServletResponse) throws IOException {
        return attachmentService.getFile(id, httpServletResponse);
    }

    @DeleteMapping("/deleteAttachment/{id}")
    public ResponseEntity<?> fileDelete(@PathVariable Long id){
        ApiResponse apiResponse=attachmentService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
