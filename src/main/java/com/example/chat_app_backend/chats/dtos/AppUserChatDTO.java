package com.example.chat_app_backend.chats.dtos;


import com.example.chat_app_backend.appUser.AppUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUserChatDTO {

    private Long id;
    private String userName;

}
