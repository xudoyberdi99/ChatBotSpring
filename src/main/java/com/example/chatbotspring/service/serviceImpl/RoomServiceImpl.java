package com.example.chatbotspring.service.serviceImpl;

import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.RoomEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.repository.ChatRepository;
import com.example.chatbotspring.repository.MessageRepository;
import com.example.chatbotspring.repository.OperatorRepository;
import com.example.chatbotspring.repository.RoomRepository;
import com.example.chatbotspring.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;


    @Override
    public ApiResponse addRoom(RoomEntity roomEntity) {
        boolean existsByName = roomRepository.existsByName(roomEntity.getName());
        if (existsByName){
            return new ApiResponse("already exists room",false);
        }
        RoomEntity room=new RoomEntity();
        room.setName(roomEntity.getName());
        roomRepository.save(room);
        return new ApiResponse("add room success",true);
    }

    @Override
    public ApiResponse editRoom(Long id,RoomEntity roomEntity) {
        Optional<RoomEntity> byIdRoom = roomRepository.findById(id);
        if (byIdRoom.isEmpty()){
            return new ApiResponse("not found room",false);
        }
        RoomEntity room = byIdRoom.get();
        room.setName(roomEntity.getName());
        roomRepository.save(room);
        return new ApiResponse("edit room success",true);
    }

    @Override
    public ApiResponse deleteRoom(Long id) {
        try {
            List<Long> operatorId = operatorRepository.findAllOperatorRoomId(id);
            for (Long aLong : operatorId) {
                UsersEntity operator = operatorRepository.getReferenceById(aLong);
                List<ChatEntity> chats = chatRepository.findAllByOperatorId(operator);
                for (ChatEntity chat : chats) {
                    messageRepository.deleteMessagesByChatId(chat.getId());
                }
                chatRepository.deletechatsByOperatorId(aLong);
            }
            operatorRepository.deleteOperatorByroomId(id);

            roomRepository.deleteById(id);


            return new ApiResponse("delete room success",true);
        }   catch (Exception e){
            return new ApiResponse("!Errorr",false);
        }
    }

    @Override
    public List<RoomEntity> getallRoom() {
        return roomRepository.findAll();
    }

    @Override
    public RoomEntity getRoom(Long id) {
        return roomRepository.findById(id).orElse(new RoomEntity());
    }
}
