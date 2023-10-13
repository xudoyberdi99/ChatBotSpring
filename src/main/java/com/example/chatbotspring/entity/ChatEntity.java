package com.example.chatbotspring.entity;

import com.example.chatbotspring.entity.base.BaseEntity;
import com.example.chatbotspring.entity.state.ChatState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.example.chatbotspring.entity.state.ChatState.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "chat")
public class ChatEntity extends BaseEntity<Long> {

    @JoinColumn(name = "clint_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "chats"})
    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity clintId;

    private String clintUsername;

    @JoinColumn(name = "operator_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity operatorId;

    private String operatorUsername;

    @JoinColumn(name = "room_id")
    @ManyToOne
    private RoomEntity room;

    @Enumerated(EnumType.STRING)
    private ChatState state = CREATE;

    public ChatEntity(UsersEntity clintId, String clintUsername, UsersEntity operatorId, String operatorUsername, RoomEntity room) {
        this.clintId = clintId;
        this.clintUsername = clintUsername;
        this.operatorId = operatorId;
        this.operatorUsername = operatorUsername;
        this.room = room;
    }
}
