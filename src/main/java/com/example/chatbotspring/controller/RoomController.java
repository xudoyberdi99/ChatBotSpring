package com.example.chatbotspring.controller;

import com.example.chatbotspring.entity.RoomEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.model.dto.OperatorDto;
import com.example.chatbotspring.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/")
public class RoomController {
    @Autowired
    private RoomService roomService;


    @PostMapping("/room/add")
    public HttpEntity<?> addRoom(@RequestBody RoomEntity roomEntity){
        ApiResponse apiResponse=roomService.addRoom(roomEntity);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @PutMapping("/room/edit/{id}")
    public HttpEntity<?> editRoom(@PathVariable Long id, @RequestBody RoomEntity roomEntity){
        ApiResponse apiResponse=roomService.editRoom(id,roomEntity);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @DeleteMapping("/room/delete/{id}")
    public HttpEntity<?> deleteRoom(@PathVariable Long id){
        ApiResponse apiResponse=roomService.deleteRoom(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @GetMapping("/room/all")
    public HttpEntity<?> getallRoom(){
        List<RoomEntity> roomEntityList=roomService.getallRoom();
        return ResponseEntity.ok(roomEntityList);
    }
    @GetMapping("/room/{id}")
    public HttpEntity<?> getRoom(@PathVariable Long id){
        RoomEntity room=roomService.getRoom(id);
        return ResponseEntity.ok(room);
    }
}
