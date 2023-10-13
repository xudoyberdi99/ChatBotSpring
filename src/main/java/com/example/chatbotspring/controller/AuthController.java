package com.example.chatbotspring.controller;


import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.model.dto.ApiResponse;
import com.example.chatbotspring.model.dto.UserLoginDTO;
import com.example.chatbotspring.security.JwtProvider;
import com.example.chatbotspring.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@CrossOrigin(value = "*",maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public HttpEntity<?> loginUser(@RequestBody UserLoginDTO loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        UsersEntity admin = (UsersEntity) authentication.getPrincipal();
        String token = jwtProvider.generateToken(admin.getUsername());
//        ApiResponse user=new ApiResponse("success",true,token);
//        return ResponseEntity.status(user.isSucces()?200:409).body(user);
        return  ResponseEntity.ok(token);
    }

}
