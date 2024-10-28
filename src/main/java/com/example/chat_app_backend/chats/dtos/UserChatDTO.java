package com.example.chat_app_backend.chats.dtos;

import com.example.chat_app_backend.chats.dtos.AppUserChatDTO;
import com.example.chat_app_backend.messages.Messages;
import com.example.chat_app_backend.messages.MessagesDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserChatDTO {

    private Long chatId;
    private AppUserChatDTO sender;
    private AppUserChatDTO receiver;
    private MessagesDTO messages;
   // private MessagesDTO messages;
}
