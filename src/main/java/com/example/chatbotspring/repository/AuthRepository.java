package com.example.chatbotspring.repository;

import com.example.chatbotspring.entity.RoomEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.entity.role.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
public interface AuthRepository extends JpaRepository<UsersEntity, Long> {
    Optional<UsersEntity> findByUsername(String username);

    Optional<UsersEntity> findByPhoneNumber(String phone);


//      List<UsersEntity> findAllByRoleAndRooms(UserRoleEnum role, RoomEntity rooms);
      List<UsersEntity> findAllByRoleAndRoomsId(UserRoleEnum role, long rooms_id);
}
