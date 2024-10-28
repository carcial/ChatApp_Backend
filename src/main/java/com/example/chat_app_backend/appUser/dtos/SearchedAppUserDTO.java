package com.example.chat_app_backend.appUser.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchedAppUserDTO {

    private Long id;
    private String userName;
    private String email;
}
