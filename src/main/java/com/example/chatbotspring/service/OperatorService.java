package com.example.chatbotspring.service;

import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.model.dto.OperatorDto;

import java.util.List;

public interface OperatorService {
    ApiResponse addOperator(OperatorDto operatorDto);

    ApiResponse editOperator(Long id, OperatorDto operatorDto);

    ApiResponse deleteOperator(Long id);

    List<UsersEntity> getallOperators();

    UsersEntity getOperator(Long id);

    List<UsersEntity> getOperatorsRoomId(Long id);
}
