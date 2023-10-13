package com.example.chatbotspring.component;


import com.example.chatbotspring.entity.RoomEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.entity.role.UserRoleEnum;
import com.example.chatbotspring.entity.state.BotState;
import com.example.chatbotspring.repository.AuthRepository;
import com.example.chatbotspring.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    AuthRepository authRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String initialModeType;


    @Override
    public void run(String... args) throws Exception {
        if(initialModeType.equals("create")){
        authRepository.save(new UsersEntity(
                "Admin","admin",
                passwordEncoder.encode("admin123"),
                "+998919999999",
                UserRoleEnum.ADMIN,null,null));


            List<RoomEntity> rooms=List.of(new RoomEntity("A Blok") , new RoomEntity("B Blok"));
            List<RoomEntity>rooms2=List.of(new RoomEntity("C Blok") , new RoomEntity("D Blok"));
            roomRepository.saveAll(rooms);
            roomRepository.saveAll(rooms2);
            UsersEntity users=new UsersEntity("Xudoyberdi","1017282415","11111","+998909244746", UserRoleEnum.OPERATOR, BotState.START,rooms);
            UsersEntity users2=new UsersEntity("Xudoyberdi2","111111","11111","+998990000000", UserRoleEnum.OPERATOR, BotState.START,rooms);
            authRepository.save(users);
            authRepository.save(users2);
        }


    }

}
