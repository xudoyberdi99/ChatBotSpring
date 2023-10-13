package com.example.chatbotspring.bot.config;

import com.example.chatbotspring.bot.BotMain;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Data
@PropertySource("application.properties")
public class TelegramConfig {
    public final BotMain botMain;

    @Bean
    public TelegramBotsApi telegramBotsApi()  {
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(botMain);
        } catch (TelegramApiException e) {

        }
        return telegramBotsApi;
    }
}
