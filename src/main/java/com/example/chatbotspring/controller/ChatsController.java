package com.example.chatbotspring.controller;

import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.model.dto.OperatorIdAndRoomIdDto;
import com.example.chatbotspring.service.ChatsService;
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
public class ChatsController {
    @Autowired
    private ChatsService chatsService;

    @GetMapping("/chats/operatorVsRoomId")
    public HttpEntity<?> getChats(@RequestParam Long roomId, @RequestParam Long operatorId){
        List<ChatEntity> chatEntityList=chatsService.getChats(roomId,operatorId);
        return ResponseEntity.ok(chatEntityList);
    }
    @DeleteMapping("/chats/{id}")
    public HttpEntity<?> deleteByChatId(@PathVariable Long id){
        ApiResponse apiResponse=chatsService.deleteChat(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


}
