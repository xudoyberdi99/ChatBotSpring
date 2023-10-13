package com.example.chatbotspring.repository;

import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.MessageEntity;
import com.example.chatbotspring.entity.state.MessageState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
    List<MessageEntity> findAllByChatIdOrderByCreatedDateDesc(long chat_id);

    List<MessageEntity> findAllByChatOrderByCreatedDate(ChatEntity chat);
    List<MessageEntity> findAllByChatIdAndStateOrderByCreatedDate(long chat_id, MessageState state);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM message WHERE chat_id IN (SELECT id FROM chat WHERE id = ?1)",nativeQuery = true)
    void deleteMessagesByChatId(Long chatId);

    List<MessageEntity> getAllByChatId(long chat_id);


}
