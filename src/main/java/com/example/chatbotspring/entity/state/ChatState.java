package com.example.chatbotspring.entity.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatState {
    CREATE,
    CONNECT,
    CLOSING;
}
