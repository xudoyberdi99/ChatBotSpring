package com.example.chatbotspring.bot;


import com.example.chatbotspring.bot.config.BotConfig;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.example.chatbotspring.entity.role.UserRoleEnum.*;
import static com.example.chatbotspring.entity.state.BotState.*;


@Component
@AllArgsConstructor
public class BotMain extends TelegramLongPollingBot {

    private final AuthService service;
    private final UserService userService;
    private final OperatorService operatorService;
    public final BotConfig botConfig;
    private static String chatId;
    private static List<BotApiMethod> methodMessage;
    private static UsersEntity users;

    private static final   String fileUrl="C:/Users/xudoy/OneDrive/Ishchi stol/ChatBotSpring/ChatBotSpring/Upload";
    private  final static  java.io.File file1=new java.io.File(fileUrl);

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onRegister() {

    }

    private void sendMessage(List<BotApiMethod> messages){

        if (messages ==null)
            return;
        messages.forEach(it->{
            try { execute(it);
                } catch (TelegramApiException ignored) {}
        });
    }


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().hasContact()){
                users=service.save(update.getMessage().getContact().getPhoneNumber(),chatId);
                users.setBotState(users.getRole()==USER? ROOM:START);
                if (users !=null && users.getRole() == USER){
                    methodMessage = userService.hasContact(update.getMessage(), users);
                }else {
                    methodMessage=operatorService.hasContact(update.getMessage(),users);
                }

            } else if (update.getMessage().hasText()){
                if(update.getMessage().getText().equals("/start")){
                    contactButton();
                }else {
                    users = service.getUserByUsername(chatId).get();
                    if (users !=null && users.getRole() == USER)
                        methodMessage = userService.hasMessage(update.getMessage(), users);
                    else if  (users !=null && users.getRole()==OPERATOR)
                        methodMessage=operatorService.hasMessage(update.getMessage(), users);
                }
            }else if (update.getMessage().hasDocument()){
                users = service.getUserByUsername(chatId).get();
                if (users !=null && users.getRole() == USER){
                    methodMessage = userService.hasDocument(update.getMessage(), users);
                    saveFile(update.getMessage());
                }else {
                    methodMessage=operatorService.hasDocument(update.getMessage(),users);
                }
            } else if (update.getMessage().hasAudio()) {
                users = service.getUserByUsername(chatId).get();
                if (users !=null && users.getRole() == USER){
                    methodMessage = userService.hasAudio(update.getMessage(), users);
                    saveFile(update.getMessage());
                }else {
                    methodMessage=operatorService.hasAudio(update.getMessage(),users);
                }
            } else if (update.getMessage().hasVideo()) {
                users = service.getUserByUsername(chatId).get();
                if (users !=null && users.getRole() == USER){
                    methodMessage = userService.hasVideo(update.getMessage(), users);
                    saveFile(update.getMessage());
                }else {
                    methodMessage=operatorService.hasVideo(update.getMessage(),users);
                }
            }else if (update.getMessage().hasVoice()){
                users = service.getUserByUsername(chatId).get();
                if (users !=null && users.getRole() == USER){
                    methodMessage = userService.hasVoice(update.getMessage(), users);
                    saveFile(update.getMessage());
                }else {
                    methodMessage=operatorService.hasVoice(update.getMessage(),users);
                }
            } else if (update.getMessage().hasPhoto()) {
                users = service.getUserByUsername(chatId).get();
                if (users !=null && users.getRole() == USER){
                    methodMessage = userService.hasPhoto(update.getMessage(), users);
                    saveFile(update.getMessage());
                }else {
                    methodMessage=operatorService.hasPhoto(update.getMessage(),users);
                }
            } else if (update.getMessage().hasVideoNote()) {
                users = service.getUserByUsername(chatId).get();
                if (users !=null && users.getRole() == USER){
                    methodMessage = userService.hasVideoNote(update.getMessage(), users);
                    saveFile(update.getMessage());
                }else {
                    methodMessage=operatorService.hasVideoNote(update.getMessage(),users);
                }
            }


        } else if (update.hasCallbackQuery()){
            update.getCallbackQuery();
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            users = service.getUserByUsername(chatId).get();
            if ( users.getRole() == USER)
                methodMessage=userService.hasCallbackQuery(update.getCallbackQuery(), users);
            else if  (users.getRole()==OPERATOR)
                methodMessage=operatorService.hasCallbackQuery(update.getCallbackQuery(), users);
        }

        sendMessage(methodMessage);
    }

    public void contactButton(){
        String text = "share your contact!";
        SendMessage message=new SendMessage(chatId,text);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // new list
        List<KeyboardRow> keyboard = new ArrayList<>();

        // first keyboard line
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Share your number >");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);

        // add array to list
        keyboard.add(keyboardFirstRow);

        // add list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(replyKeyboardMarkup);
        methodMessage=List.of(message);

    }

    public void saveFile(Message message){
        long size=1024*1024*50;
        if (message.hasDocument() && message.getDocument().getFileSize()<size){
            Document document=message.getDocument();
            String fil= document.getFileUniqueId()+ "." +document.getMimeType().split("/")[1];
            Path filePath = Paths.get(fileUrl, fil);
            if (filePath.toFile().exists()) {
                return;
            }
        try {
            GetFile getFile=new GetFile();
            getFile.setFileId(document.getFileId());
            File file = execute(getFile);

            // Download the file contents from the Telegram servers
            URL url = new URL("https://api.telegram.org/file/bot" + botConfig.getToken() + "/" + file.getFilePath());
            InputStream in = url.openStream();

            // Save the file to the file system
            String fileName =fileUrl+ "/" + fil;

            FileOutputStream out = new FileOutputStream(fileName);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.close();

        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
        }

        else if (message.hasAudio()) {
            Audio audio = message.getAudio();
            String fil= audio.getFileUniqueId()+ "." +audio.getMimeType().split("/")[1];
            Path filePath = Paths.get(fileUrl, fil);
            if (filePath.toFile().exists()) {
                return;
            }
            try {
                GetFile getFile=new GetFile();
                getFile.setFileId(audio.getFileId());
                File file = execute(getFile);

                // Download the file contents from the Telegram servers
                URL url = new URL("https://api.telegram.org/file/bot" + botConfig.getToken() + "/" + file.getFilePath());
                InputStream in = url.openStream();

                // Save the file to the file system
                String fileName =fileUrl+ "/" + fil;

                FileOutputStream out = new FileOutputStream(fileName);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                in.close();
                out.close();

            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }

        else if (message.hasVoice()) {
            Voice voice = message.getVoice();
            String fil= voice.getFileUniqueId()+ "." +voice.getMimeType().split("/")[1];
            Path filePath = Paths.get(fileUrl, fil);
            if (filePath.toFile().exists()) {
                return;
            }
            try {
                GetFile getFile=new GetFile();
                getFile.setFileId(voice.getFileId());
                File file = execute(getFile);

                // Download the file contents from the Telegram servers
                URL url = new URL("https://api.telegram.org/file/bot" + botConfig.getToken() + "/" + file.getFilePath());
                InputStream in = url.openStream();

                // Save the file to the file system
                String fileName =fileUrl+ "/" + fil;

                FileOutputStream out = new FileOutputStream(fileName);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                in.close();
                out.close();

            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }

        else if (message.hasVideo()) {
            Video video = message.getVideo();
            String fil= video.getFileUniqueId()+ "." +video.getMimeType().split("/")[1];
            Path filePath = Paths.get(fileUrl, fil);
            if (filePath.toFile().exists()) {
                return;
            }
            try {
                GetFile getFile=new GetFile();
                getFile.setFileId(video.getFileId());
                File file = execute(getFile);

                // Download the file contents from the Telegram servers
                URL url = new URL("https://api.telegram.org/file/bot" + botConfig.getToken() + "/" + file.getFilePath());
                InputStream in = url.openStream();

                // Save the file to the file system
                String fileName =fileUrl+ "/" + fil;

                FileOutputStream out = new FileOutputStream(fileName);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                in.close();
                out.close();

            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }

        }

        else if (message.hasPhoto()){
            List<PhotoSize> photos = message.getPhoto();
            PhotoSize photo = photos.get(photos.size() - 1);
            String contentType="png";
            String fil= photo.getFileUniqueId()+ "." +contentType;
            Path filePath = Paths.get(fileUrl, fil);
            if (filePath.toFile().exists()) {
                return;
            }
            try {
                GetFile getFile=new GetFile();

                getFile.setFileId(photo.getFileId());
                File file = execute(getFile);

                // Download the file contents from the Telegram servers
                URL url = new URL("https://api.telegram.org/file/bot" + botConfig.getToken() + "/" + file.getFilePath());
                InputStream in = url.openStream();

                // Save the file to the file system
                String fileName =fileUrl+ "/" + fil;

                FileOutputStream out = new FileOutputStream(fileName);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                in.close();
                out.close();

            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }

        else if (message.hasVideoNote()){
            VideoNote videoNote = message.getVideoNote();
            String contentType="mp4";
            String fil= videoNote.getFileUniqueId()+ "."+contentType;
            Path filePath = Paths.get(fileUrl, fil);
            if (filePath.toFile().exists()) {
                return;
            }
            try {
                GetFile getFile=new GetFile();

                getFile.setFileId(videoNote.getFileId());
                File file = execute(getFile);

                // Download the file contents from the Telegram servers
                URL url = new URL("https://api.telegram.org/file/bot" + botConfig.getToken() + "/" + file.getFilePath());
                InputStream in = url.openStream();

                // Save the file to the file system
                String fileName =fileUrl+ "/" + fil;

                FileOutputStream out = new FileOutputStream(fileName);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                in.close();
                out.close();

            } catch (TelegramApiException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
