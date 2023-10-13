package com.example.chatbotspring.entity;

import com.example.chatbotspring.entity.base.BaseEntity;
import com.example.chatbotspring.entity.state.MessageState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "message")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handle"})
public class MessageEntity extends BaseEntity<Long> {

    @JoinColumn(name = "chat_id")
    @ManyToOne
    private ChatEntity chat;

    @JoinColumn(name = "users_id")
    @ManyToOne
    private UsersEntity users;

    private Integer messageId;

    @OneToMany(mappedBy = "message")
    private List<AttachmentEntity> attachment;
    private String text;

    @Enumerated(EnumType.STRING)
    private MessageState state=MessageState.UNREAD;

    public MessageEntity(ChatEntity chat, UsersEntity users,Integer messageId, String text) {
        this.chat = chat;
        this.users = users;
        this.messageId=messageId;
        this.text = text;
    }


    public MessageEntity(ChatEntity chat, UsersEntity users,Integer messageId, String text, MessageState read) {
        this.chat = chat;
        this.users = users;
        this.messageId=messageId;
        this.text = text;
        this.state=read;
    }
}
