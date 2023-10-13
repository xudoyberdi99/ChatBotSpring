package com.example.chatbotspring.controller;


import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.model.dto.OperatorDto;
import com.example.chatbotspring.service.OperatorService;
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
public class OperatorController {
    @Autowired
    private OperatorService operatorService;


    @PostMapping("/operator/add")
    public HttpEntity<?> addOperator(@RequestBody OperatorDto operatorDto){
        ApiResponse apiResponse=operatorService.addOperator(operatorDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @PutMapping("/operator/edit/{id}")
    public HttpEntity<?> editOperator(@PathVariable Long id, @RequestBody OperatorDto operatorDto){
        ApiResponse apiResponse=operatorService.editOperator(id,operatorDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @DeleteMapping("/operator/delete/{id}")
    public HttpEntity<?> deleteOperator(@PathVariable Long id){
        ApiResponse apiResponse=operatorService.deleteOperator(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
    @GetMapping("/operator/all")
    public HttpEntity<?> getallOperators(){
        List<UsersEntity> operatorList=operatorService.getallOperators();
        return ResponseEntity.ok(operatorList);
    }
    @GetMapping("/operator/{id}")
    public HttpEntity<?> getOperator(@PathVariable Long id){
        UsersEntity operator=operatorService.getOperator(id);
        return ResponseEntity.ok(operator);
    }
    @GetMapping("/operators/roomId/{id}")
    public HttpEntity<?> getOperatorsRoomId(@PathVariable Long id){
        List<UsersEntity> operators=operatorService.getOperatorsRoomId(id);
        return ResponseEntity.ok(operators);
    }
}
