package com.example.chatbotspring.controller;

import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.MessageEntity;
import com.example.chatbotspring.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/messages/{chatId}")
    public HttpEntity<?> getMessages(@PathVariable Long chatId){
        List<MessageEntity> messageEntities=messageService.getMessages(chatId);
        return ResponseEntity.ok(messageEntities);
    }
    @GetMapping("/messagess/{id}")
    public HttpEntity<?> getMessagesId(@PathVariable Long id){
        MessageEntity messageEntities=messageService.getMessagesId(id);
        return ResponseEntity.ok(messageEntities);
    }
}
