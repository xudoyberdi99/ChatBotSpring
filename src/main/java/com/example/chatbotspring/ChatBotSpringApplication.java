package com.example.chatbotspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ChatBotSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatBotSpringApplication.class, args);
    }

}
