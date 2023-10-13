package com.example.chatbotspring.repository;

import com.example.chatbotspring.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {



    List<RoomEntity> findAllBy();
    boolean existsByName(String name);
}
