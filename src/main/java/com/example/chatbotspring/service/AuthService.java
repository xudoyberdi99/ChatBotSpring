package com.example.chatbotspring.service;

import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service("authService1")
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthRepository repository;

    public Optional<UsersEntity> getUserByUsername(String username){
        return repository.findByUsername(username);
    }

    public UsersEntity save(String phoneNumber, String chatId){

        Optional<UsersEntity> byPhoneNumber = repository.findByPhoneNumber(phoneNumber);
        if (byPhoneNumber.isPresent())
            return byPhoneNumber.get();
        UsersEntity users=new UsersEntity();
                users.setPhoneNumber(phoneNumber);
                users.setUsername(chatId);
        return repository.save(users);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username+" topilmadi"));
    }
}
