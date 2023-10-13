package com.example.chatbotspring.model.dto;

import com.example.chatbotspring.entity.role.UserRoleEnum;
import lombok.Data;

import java.util.List;

@Data
public class UserRegisterDto {

    private String username;
    private String password;
    private UserRoleEnum roles;

}
