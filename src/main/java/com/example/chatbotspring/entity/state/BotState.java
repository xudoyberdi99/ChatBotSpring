package com.example.chatbotspring.entity.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BotState {
    START,
    ROOM,
    CHOICE_OPERATOR,
    CHAT,
    OPERATOR_WINDOW;
}
