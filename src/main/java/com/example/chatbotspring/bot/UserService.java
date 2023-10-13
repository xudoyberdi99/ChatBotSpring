package com.example.chatbotspring.bot;

import com.example.chatbotspring.entity.*;
import com.example.chatbotspring.entity.MessageEntity;
import com.example.chatbotspring.entity.role.UserRoleEnum;
import com.example.chatbotspring.entity.state.ChatState;
import com.example.chatbotspring.entity.state.MessageState;
import com.example.chatbotspring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.chatbotspring.entity.state.BotState.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final RoomRepository roomRepository;

    private final ChatRepository chatRepository;

    private final AuthRepository authRepository;
    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;
    private static String chatId;
    public static String text;
    public static List<BotApiMethod>  response;

    public List<BotApiMethod> hasMessage(Message message,  UsersEntity users){
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case ROOM -> { response=List.of(new DeleteMessage(chatId,message.getMessageId())); new SendMessage(chatId,"Choice Rooms"); }
            case CHOICE_OPERATOR -> { response=List.of(new DeleteMessage(chatId,message.getMessageId())); new SendMessage(chatId,"Choice Operator");}
            case CHAT -> { saveMessage(users, message); }
        }
        authRepository.save(users);
        return response;
    }

    public List<BotApiMethod> hasContact(Message message,  UsersEntity users){
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case ROOM -> {
                choiceRoom();
            }
        }
        authRepository.save(users);
        return response;
    }

    public List<BotApiMethod> hasCallbackQuery(CallbackQuery  message, UsersEntity users){
        String data = message.getData();
        chatId=message.getMessage().getChatId().toString();

        switch (users.getBotState()){
            case ROOM -> {
                choiceOperator(data,message);
                users.setBotState(CHOICE_OPERATOR);
            }
            case CHOICE_OPERATOR -> {
                String[] split = data.split(":");
                var room = roomRepository.findById(Long.parseLong(split[0])).orElse(new RoomEntity());
                var operator = authRepository.findById(Long.parseLong(split[1])).get();
                chatRepository.save(new ChatEntity(users, users.getUsername(),operator,operator.getUsername(),room));
                response=List.of(new DeleteMessage(chatId,message.getMessage().getMessageId()),new SendMessage(chatId,"Savolingizni yuboring, Operatorlarimiz tez orada javob yullaydi sizga"));
                users.setBotState(CHAT);
            }
        }
        authRepository.save(users);
        return response;
    }
    public List<BotApiMethod> hasDocument(Message message, UsersEntity users){
        Document document=message.getDocument();
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case CHAT -> {
                saveMessage(users,message);
            }default -> response=List.of(new DeleteMessage(chatId,message.getMessageId()));

        }
        authRepository.save(users);
        return response;
    }
    public List<BotApiMethod> hasAudio(Message message, UsersEntity users){
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case CHAT -> {
                saveMessage(users,message);
            }default -> response=List.of(new DeleteMessage(chatId,message.getMessageId()));

        }
        authRepository.save(users);
        return response;
    }

    public List<BotApiMethod> hasVideo(Message message, UsersEntity users){
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case CHAT -> {
                saveMessage(users,message);
            }default -> response=List.of(new DeleteMessage(chatId,message.getMessageId()));

        }
        authRepository.save(users);
        return response;
    }
    public List<BotApiMethod> hasVoice(Message message, UsersEntity users){
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case CHAT -> {
                saveMessage(users,message);
            }default -> response=List.of(new DeleteMessage(chatId,message.getMessageId()));

        }
        authRepository.save(users);
        return response;
    }
    public List<BotApiMethod> hasPhoto(Message message, UsersEntity users){
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case CHAT -> {
                saveMessage(users,message);
            }default -> response=List.of(new DeleteMessage(chatId,message.getMessageId()));

        }
        authRepository.save(users);
        return response;
    }
    public List<BotApiMethod> hasVideoNote(Message message, UsersEntity users){
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case CHAT -> {
                saveMessage(users,message);
            }default -> response=List.of(new DeleteMessage(chatId,message.getMessageId()));

        }
        authRepository.save(users);
        return response;
    }
    public void choiceRoom(){
        text="Choice Room";
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list  = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);


        roomRepository.findAllBy().forEach(it->{
            List<InlineKeyboardButton> inlineKeyboardButtons=new ArrayList<>() ;
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(it.getName());
            inlineKeyboardButton.setCallbackData(Long.toString(it.getId()));
            inlineKeyboardButtons.add(inlineKeyboardButton);
            list.add(inlineKeyboardButtons);
        });

        SendMessage sendMessage=new SendMessage(chatId,text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        response=List.of(sendMessage);
    }

    public void choiceOperator(String room,CallbackQuery  message){
        System.out.println(room);
        text="Choice Operator";
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list  = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        authRepository.findAllByRoleAndRoomsId(UserRoleEnum.OPERATOR,Long.parseLong(room)).forEach(it->{
            List<InlineKeyboardButton> inlineKeyboardButtons=new ArrayList<>() ;
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(it.getName()!=null? it.getName():"operator "+it.getId());
            inlineKeyboardButton.setCallbackData(room+":"+Long.toString(it.getId()));
            inlineKeyboardButtons.add(inlineKeyboardButton);
            list.add(inlineKeyboardButtons);
        });

        DeleteMessage deleteMessage=new DeleteMessage(chatId,message.getMessage().getMessageId());
        SendMessage sendMessage=new SendMessage(chatId,text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        response=List.of(deleteMessage,sendMessage);
    }

    public void saveMessage(UsersEntity users,Message message){
        var chat = chatRepository.findTopByClintIdAndStateNotOrderByCreatedDateDesc(users, ChatState.CLOSING).get();
        MessageEntity messageEntity;
        if (chat.getState()==ChatState.CREATE){
            response=null;
            messageEntity=new MessageEntity(chat,users,message.getMessageId(),message.getText());
        }else {
            response=List.of(new ForwardMessage(chat.getOperatorUsername(),chatId, message.getMessageId()));
            messageEntity=new MessageEntity(chat,users,message.getMessageId(), message.getText(), MessageState.READ);
        }
        messageRepository.save(messageEntity);
        if (message.hasDocument()){
            Document document=message.getDocument();
            AttachmentEntity attachment=new AttachmentEntity();
            attachment.setFileName(document.getFileUniqueId()+"."+document.getMimeType().split("/")[1]);
            attachment.setName(document.getFileName());
            attachment.setSize(document.getFileSize());
            attachment.setContentType(document.getMimeType().split("/")[1]);
            attachment.setMessage(messageEntity);

            attachmentRepository.save(attachment);

        } else if (message.hasAudio()) {
            Audio audio=message.getAudio();
            AttachmentEntity attachment=new AttachmentEntity();
            attachment.setFileName(audio.getFileUniqueId()+"."+audio.getMimeType().split("/")[1]);
            attachment.setName(audio.getFileName());
            attachment.setSize(audio.getFileSize());
            attachment.setContentType(audio.getMimeType().split("/")[1]);
            attachment.setMessage(messageEntity);
            attachmentRepository.save(attachment);
        }else if (message.hasVoice()) {
            Voice voice = message.getVoice();
            AttachmentEntity attachment=new AttachmentEntity();
            attachment.setFileName(voice.getFileUniqueId()+"."+voice.getMimeType().split("/")[1]);
            attachment.setName(voice.getFileId()); /* kurish */
            attachment.setSize(voice.getFileSize());
            attachment.setContentType(voice.getMimeType().split("/")[1]);
            attachment.setMessage(messageEntity);
            attachmentRepository.save(attachment);
        }else if (message.hasVideo()) {
            Video video = message.getVideo();
            AttachmentEntity attachment=new AttachmentEntity();
            attachment.setFileName(video.getFileUniqueId()+"."+video.getMimeType().split("/")[1]);
            attachment.setName(video.getFileName());
            attachment.setSize(video.getFileSize());
            attachment.setContentType(video.getMimeType().split("/")[1]);
            attachment.setMessage(messageEntity);
            attachmentRepository.save(attachment);
        }else if (message.hasPhoto()){
            List<PhotoSize> photos = message.getPhoto();
            PhotoSize photo = photos.get(photos.size() - 1);
            String contentType="png";
            AttachmentEntity attachment=new AttachmentEntity();
            attachment.setFileName(photo.getFileUniqueId()+"."+contentType);
            attachment.setName(photo.getFileUniqueId());
            attachment.setSize(Long.valueOf(photo.getFileSize()));
            attachment.setContentType(contentType);
            attachment.setMessage(messageEntity);
            attachmentRepository.save(attachment);
        } else if (message.hasVideoNote()) {
            VideoNote videoNote = message.getVideoNote();
            String contentType="mp4";
            AttachmentEntity attachment=new AttachmentEntity();
            attachment.setFileName(videoNote.getFileUniqueId()+"."+contentType);
            attachment.setName(videoNote.getFileUniqueId());
            attachment.setSize(Long.valueOf(videoNote.getFileSize()));
            attachment.setContentType(contentType);
            attachment.setMessage(messageEntity);
            attachmentRepository.save(attachment);

        }

    }

}
