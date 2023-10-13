package com.example.chatbotspring.service.serviceImpl;

import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.RoomEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.entity.role.UserRoleEnum;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.model.dto.OperatorDto;
import com.example.chatbotspring.repository.ChatRepository;
import com.example.chatbotspring.repository.MessageRepository;
import com.example.chatbotspring.repository.OperatorRepository;
import com.example.chatbotspring.repository.RoomRepository;
import com.example.chatbotspring.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OperatorServiceImpl implements OperatorService {
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public ApiResponse addOperator(OperatorDto operatorDto) {
        boolean exists = operatorRepository.existsByPhoneNumber(operatorDto.getPhoneNumber());
        if (exists){
            return new ApiResponse("already exists phoneNumber",false);
        }
        List<RoomEntity> roomEntityList=new ArrayList<>();
        for (Long room : operatorDto.getRooms()) {
            RoomEntity referenceById = roomRepository.getReferenceById(room);
            roomEntityList.add(referenceById);
        }

        UsersEntity usersEntity=new UsersEntity();
        usersEntity.setName(operatorDto.getName());
        usersEntity.setPhoneNumber(operatorDto.getPhoneNumber());
        usersEntity.setRole(UserRoleEnum.OPERATOR);
        usersEntity.setRooms(roomEntityList);

        operatorRepository.save(usersEntity);
        return new ApiResponse("add success operator",true);
    }

    @Override
    public ApiResponse editOperator(Long id, OperatorDto operatorDto) {
        Optional<UsersEntity> operatorRepositoryById = operatorRepository.findById(id);
        if (operatorRepositoryById.isEmpty()){
            return new ApiResponse("not found operator",false);
        }
        List<RoomEntity> roomEntityList=new ArrayList<>();
        for (Long room : operatorDto.getRooms()) {
            RoomEntity referenceById = roomRepository.getReferenceById(room);
            roomEntityList.add(referenceById);
        }

        UsersEntity usersEntity = operatorRepositoryById.get();
        usersEntity.setName(operatorDto.getName());
        usersEntity.setPhoneNumber(operatorDto.getPhoneNumber());
        usersEntity.setRole(UserRoleEnum.OPERATOR);
        usersEntity.setRooms(roomEntityList);
        operatorRepository.save(usersEntity);
        return new ApiResponse("edit operator success",true);
    }

    @Override
    public ApiResponse deleteOperator(Long id) {
        try {
            Optional<UsersEntity> repository = operatorRepository.findById(id);
            if (repository.isEmpty()){
                return new ApiResponse("not found operator", false);
            }
            UsersEntity usersEntity = repository.get();
            List<ChatEntity> chatEntities = chatRepository.findAllByOperatorId(usersEntity);
            for (ChatEntity chatEntity : chatEntities) {
                messageRepository.deleteMessagesByChatId(chatEntity.getId());
            }

            chatRepository.deletechatsByOperatorId(id);

            operatorRepository.deleteById(id);
            return new ApiResponse("delete operator success",true);
        }catch (Exception e){
            return new ApiResponse("Error!!!",false);
        }
    }

    @Override
    public List<UsersEntity> getallOperators() {
        List<UsersEntity> users = operatorRepository.findAll();
        List<UsersEntity> operator=new ArrayList<>();
        for (UsersEntity user : users) {
            if (user.getRole().equals(UserRoleEnum.OPERATOR)){
                operator.add(user);
            }
        }
        return operator;
    }

    @Override
    public UsersEntity getOperator(Long id) {
        return operatorRepository.findById(id).orElse(new UsersEntity());
    }

    @Override
    public List<UsersEntity> getOperatorsRoomId(Long id) {
        List<UsersEntity> allByRoomsId = operatorRepository.findAllByRoomsId(id);
        List<UsersEntity> operators=new ArrayList<>();
        for (UsersEntity usersEntity : allByRoomsId) {
            if (usersEntity.getRole().equals(UserRoleEnum.OPERATOR)){
                operators.add(usersEntity);
            }
        }
        return operators;
    }
}
