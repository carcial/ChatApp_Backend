package com.example.chat_app_backend.appUser.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDTO {

    private Long id;

    private Long profilePic_imageId;
    private String userName;
    private String email;
    private Set<AppUserDTO> friends;
    private Set<AppUserDTO> sendFriendInvitationTO;
    private Set<AppUserDTO> receiveFriendInvitationFROM;

    public AppUserDTO(Long id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }
}
