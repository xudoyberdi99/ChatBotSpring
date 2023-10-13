package com.example.chatbotspring.service.serviceImpl;

import com.example.chatbotspring.entity.AttachmentEntity;
import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.MessageEntity;
import com.example.chatbotspring.repository.AttachmentRepository;
import com.example.chatbotspring.repository.ChatRepository;
import com.example.chatbotspring.repository.MessageRepository;
import com.example.chatbotspring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessagesServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Override
    public List<MessageEntity> getMessages(Long chatId) {
        Optional<ChatEntity> chatEntity = chatRepository.findById(chatId);
        List<MessageEntity> messageEntities = messageRepository.findAllByChatOrderByCreatedDate(chatEntity.get());
        List<MessageEntity> messagess=new ArrayList<>();
        for (MessageEntity messageEntity : messageEntities) {
            List<AttachmentEntity> attachs = attachmentRepository.findAllByMessage(messageEntity);
            System.out.println(attachs);
            messageEntity.setAttachment(attachs);
            messagess.add(messageEntity);
        }
        return messagess;
    }

    @Override
    public MessageEntity getMessagesId(Long id) {
        return messageRepository.findById(id).orElse(new MessageEntity());
    }
}
