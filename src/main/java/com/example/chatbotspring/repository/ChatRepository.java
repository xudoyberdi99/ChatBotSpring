package com.example.chatbotspring.repository;

import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.entity.state.ChatState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity,Long> {
//    Optional<ChatEntity> findByClintIdAndStateNot(UsersEntity clintId, ChatState state);
    Optional<ChatEntity> findTopByClintIdAndStateNotOrderByCreatedDateDesc(UsersEntity clintId, ChatState state);
    Optional<ChatEntity> findTopByOperatorIdAndState(UsersEntity operatorId, ChatState state);

    List<ChatEntity> findAllByRoomIdAndOperatorIdOrderByCreatedDate(long room_id, UsersEntity operatorId);
    Optional<ChatEntity> findByOperatorIdAndState(UsersEntity operatorId, ChatState state);
    List<ChatEntity> findAllByOperatorIdAndStateNotOrderByCreatedDate(UsersEntity operatorId, ChatState state);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM chat WHERE operator_id IN (SELECT id FROM users WHERE id = ?1)",nativeQuery = true)
    void deletechatsByOperatorId(Long chatId);

    List<ChatEntity> findAllByOperatorId(UsersEntity operatorId);
}
