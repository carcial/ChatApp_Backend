package com.example.chat_app_backend.appUser.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvitationDTO {

    SearchedAppUserDTO sender;
    SearchedAppUserDTO receiver;
}
