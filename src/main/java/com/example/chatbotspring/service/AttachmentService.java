package com.example.chatbotspring.service;


import com.example.chatbotspring.entity.AttachmentEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface AttachmentService {
    ApiResponse addAttachment(MultipartHttpServletRequest request) throws IOException;

    Page<AttachmentEntity> findAllWithPage(int page, int size);

    List<AttachmentEntity> findAllFiles();

    AttachmentEntity downloadToServer(Long id, HttpServletResponse response) throws IOException;

    ApiResponse delete(Long id);

    ResponseEntity<InputStreamResource> getFile(Long id, HttpServletResponse httpServletResponse) throws FileNotFoundException;


}
