package com.example.chatbotspring.repository;


import com.example.chatbotspring.entity.AttachmentEntity;
import com.example.chatbotspring.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity,Long> {
        List<AttachmentEntity> findAllByMessage(MessageEntity message);
}
