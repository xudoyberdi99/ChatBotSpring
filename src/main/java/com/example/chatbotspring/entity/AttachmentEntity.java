package com.example.chatbotspring.entity;

import com.example.chatbotspring.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "attachment")
public class AttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "message_id")
    @ManyToOne
    private MessageEntity message;

    private String name;
    private String fileName;
    private String contentType;
    private Long size;
}
