package com.example.chatbotspring.service;

import com.example.chatbotspring.entity.RoomEntity;
import com.example.chatbotspring.model.dto.ApiResponse;

import java.util.List;

public interface RoomService {
    ApiResponse addRoom(RoomEntity roomEntity);

    ApiResponse editRoom(Long id,RoomEntity roomEntity);

    ApiResponse deleteRoom(Long id);

    List<RoomEntity> getallRoom();

    RoomEntity getRoom(Long id);
}
