package com.example.chatbotspring.entity.role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRoleEnum {

    SUPER_ADMIN,
    ADMIN,
    OPERATOR,
    USER;
}
