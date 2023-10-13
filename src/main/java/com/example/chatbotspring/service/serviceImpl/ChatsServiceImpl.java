package com.example.chatbotspring.service.serviceImpl;

import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.repository.ChatRepository;
import com.example.chatbotspring.repository.MessageRepository;
import com.example.chatbotspring.repository.OperatorRepository;
import com.example.chatbotspring.service.ChatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatsServiceImpl implements ChatsService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<ChatEntity> getChats(Long roomId, Long operatorId) {
        Optional<UsersEntity> usersEntity = operatorRepository.findById(operatorId);
        if (usersEntity.isEmpty()){
            return null;
        }
        UsersEntity usersEntity1 = usersEntity.get();
        return chatRepository.findAllByRoomIdAndOperatorIdOrderByCreatedDate(roomId, usersEntity1);
    }

    @Override
    public ApiResponse deleteChat(Long id) {
        try {
            messageRepository.deleteMessagesByChatId(id);
            chatRepository.deleteById(id);
            return new ApiResponse("Delete chat success",true);
        }catch (Exception e){
            return new ApiResponse("Errorr delete chat",false);
        }
    }
}
