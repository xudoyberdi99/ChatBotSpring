package com.example.chatbotspring.service;

import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.model.dto.OperatorIdAndRoomIdDto;

import java.util.List;

public interface ChatsService {
    List<ChatEntity> getChats(Long roomId,Long operatorId);

    ApiResponse deleteChat(Long id);

}
