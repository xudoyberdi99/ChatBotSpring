package com.example.chatbotspring.model.dto;

import com.example.chatbotspring.entity.RoomEntity;
import lombok.Data;

import java.util.List;

@Data
public class OperatorDto {
    private String name;
    private String phoneNumber;
    private List<Long> rooms;
}
