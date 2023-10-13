package com.example.chatbotspring.service;

import com.example.chatbotspring.entity.MessageEntity;

import java.util.List;

public interface MessageService {
    List<MessageEntity> getMessages(Long chatId);

    MessageEntity getMessagesId(Long id);
}
