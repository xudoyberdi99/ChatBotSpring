package com.example.chatbotspring.bot;

import com.example.chatbotspring.entity.AttachmentEntity;
import com.example.chatbotspring.entity.ChatEntity;
import com.example.chatbotspring.entity.MessageEntity;
import com.example.chatbotspring.entity.UsersEntity;
import com.example.chatbotspring.entity.role.UserRoleEnum;
import com.example.chatbotspring.entity.state.BotState;
import com.example.chatbotspring.entity.state.ChatState;
import com.example.chatbotspring.entity.state.MessageState;
import com.example.chatbotspring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.chatbotspring.entity.state.BotState.*;

@Service
@RequiredArgsConstructor
public class OperatorService {
    private final AuthRepository authRepository;
    private final RoomRepository roomRepository;
    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;
    private static String chatId;
    private static String text;
    private final ChatRepository chatRepository;
    public static List<BotApiMethod> response;

    public List<BotApiMethod> hasMessage(Message message,  UsersEntity users){
        chatId=message.getChatId().toString();
        System.out.println(message.getText());
        if (message.getText().equals("Chat_List")){
            chatList(users,message);
            users.setBotState(OPERATOR_WINDOW);
            authRepository.save(users);
        } else if (message.getText().equals("Close")) {
            Optional<ChatEntity> byOperatorIdAndState = chatRepository.findByOperatorIdAndState(users, ChatState.CONNECT);
            if (byOperatorIdAndState.isPresent()){
                ChatEntity chatEntity = byOperatorIdAndState.get();
                chatEntity.setState(ChatState.CLOSING);
                Optional<UsersEntity> client = authRepository.findById(chatEntity.getClintId().getId());
                if (client.isPresent()){
                    UsersEntity usersEntity = client.get();
                    usersEntity.setBotState(ROOM);
                    choiceRoom(usersEntity.getUsername());
                    authRepository.save(usersEntity);
                }
                chatRepository.save(chatEntity);
            }
        }

        switch (users.getBotState()){
            case CHAT -> {
                saveMessage(users,message);
            }
        }
        return response;
    }
    public List<BotApiMethod> hasContact(Message message,  UsersEntity users){
        chatId=message.getChatId().toString();
        switch (users.getBotState()){
            case START -> {
                operatorWindow();
                users.setBotState(OPERATOR_WINDOW);
            }
        }
        authRepository.save(users);
        return response;
    }

    public List<BotApiMethod> hasCallbackQuery(CallbackQuery message, UsersEntity users){
        String Idchat = message.getData();
        chatId=message.getMessage().getChatId().toString();
        switch (users.getBotState()){
            case OPERATOR_WINDOW -> {
                chatCreateFromConnnect(users);
                chatConnectFromCreate(Long.parseLong(Idchat));
                users.setBotState(CHAT);
            }
        }
        authRepository.save(users);
        return response;
    }
    public void chatCreateFromConnnect(UsersEntity users){
        Optional<ChatEntity> byOperatorIdAndState = chatRepository.findByOperatorIdAndState(users, ChatState.CONNECT);
        if (byOperatorIdAndState.isEmpty()){
            return;
        }
        ChatEntity chatEntity = byOperatorIdAndState.get();
        chatEntity.setState(ChatState.CREATE);
        chatRepository.save(chatEntity);
    }

    public void chatConnectFromCreate(Long IdChat){
        Optional<ChatEntity> chatRepositoryById = chatRepository.findById(IdChat);
        if (chatRepositoryById.isEmpty()){
            return;
        }
        ChatEntity chat = chatRepositoryById.get();
        chat.setState(ChatState.CONNECT);
        chatRepository.save(chat);
        response=new ArrayList<>();
//        ArrayList<ForwardMessage> forwardMessages=new ArrayList<>();
        List<MessageEntity> messageEntities = messageRepository.findAllByChatIdAndStateOrderByCreatedDate(IdChat, MessageState.UNREAD);
        for (MessageEntity messageEntity : messageEntities) {
                ForwardMessage forwardMessage=new ForwardMessage(chat.getOperatorUsername(),chat.getClintUsername(),messageEntity.getMessageId());
                response.add(forwardMessage);
                messageEntity.setState(MessageState.READ);
        }
        messageRepository.saveAll(messageEntities);
    }

    public void chatList(UsersEntity users,Message message){
        text="chatni tanlang";
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list  = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        chatRepository.findAllByOperatorIdAndStateNotOrderByCreatedDate(users,ChatState.CLOSING).forEach(it->{
            List<InlineKeyboardButton> inlineKeyboardButtons=new ArrayList<>() ;
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("user_id : "+it.getClintUsername()+" ******* createdDate: "+it.getCreatedDate());
            inlineKeyboardButton.setCallbackData(Long.toString(it.getId()));
            inlineKeyboardButtons.add(inlineKeyboardButton);
            list.add(inlineKeyboardButtons);
        });
        DeleteMessage deleteMessage=new DeleteMessage(chatId,message.getMessageId());
        SendMessage sendMessage=new SendMessage(chatId,text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        response=List.of(deleteMessage,sendMessage);
    }

    public void operatorWindow(){
        String text="iltimos chatlarni ko'rish uchun chatlistni bosing";
        // Keyboard knopkalarni yaratish
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                // Knopkalar soni: 2
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        // Keyboard knopkalarni yaratish
        List<KeyboardRow> keyboardButtons = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        // chatlist button yaratish
        KeyboardButton chatListButton = new KeyboardButton();
        chatListButton.setText("Chat_List");
        keyboardRow.add(chatListButton);

        // close button yaratish
        KeyboardButton closeButton = new KeyboardButton();
        closeButton.setText("Close");
        keyboardRow.add(closeButton);

        // knopkalarni qo'shish
        keyboardButtons.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardButtons);

        SendMessage sendMessage=new SendMessage(chatId,text);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        response=List.of(sendMessage);
    }

    public List<BotApiMethod> hasDocument(Message message, UsersEntity users){
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


    public void saveMessage(UsersEntity operator,Message message) {
        var chat1 = chatRepository.findByOperatorIdAndState(operator, ChatState.CONNECT);
        if (chat1.isEmpty()){
            return;
        }
        ChatEntity chat = chat1.get();
        MessageEntity messageEntity;
            response = List.of(new CopyMessage(chat.getClintUsername(), chatId, message.getMessageId()));
            messageEntity = new MessageEntity(chat, operator,message.getMessageId(), message.getText(), MessageState.READ);

        messageRepository.save(messageEntity);
        if (message.hasDocument()) {
            Document document = message.getDocument();
            AttachmentEntity attachment = new AttachmentEntity();
            attachment.setFileName(document.getFileUniqueId() + "." + document.getMimeType().split("/")[1]);
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
        }else if (message.hasVoice()){
            Voice voice = message.getVoice();
            AttachmentEntity attachment=new AttachmentEntity();
            attachment.setFileName(voice.getFileUniqueId()+"."+voice.getMimeType().split("/")[1]);
            attachment.setName(voice.getFileId());  /*  kurish kerak */
            attachment.setSize(voice.getFileSize());
            attachment.setContentType(voice.getMimeType().split("/")[1]);
            attachment.setMessage(messageEntity);
            attachmentRepository.save(attachment);
        }else if (message.hasAudio()) {
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
        }else if (message.hasVideoNote()) {
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

    public void choiceRoom(String IdChat){
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

        SendMessage sendMessage=new SendMessage(IdChat,text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        response=List.of(sendMessage);
    }
}
