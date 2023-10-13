package com.example.chatbotspring.repository;

import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.entity.role.UserRoleEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperatorRepository extends JpaRepository<UsersEntity,Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    List<UsersEntity> findAllByRoomsId(Long roomId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users_rooms WHERE rooms_id IN (SELECT id FROM room WHERE id = ?1)",nativeQuery = true)
    void deleteOperatorByroomId(Long roomId);


    @Query(value = "select * from users_rooms where rooms_id=?1",nativeQuery = true)
    List<Long> findAllOperatorRoomId(Long roomId);
}
