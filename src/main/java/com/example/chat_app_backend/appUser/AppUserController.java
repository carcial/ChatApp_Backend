package com.example.chat_app_backend.appUser;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class AppUserController {

    private final AppUserService appUserService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AppUser appUser) {
        appUserService.addUser(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration Successful");
    }

    @PostMapping("/login")
    public  ResponseEntity<AppUserDTO> login(@RequestBody AppUser appUser){
        try {
            AppUserDTO appUserDTO = appUserService.login(appUser);
            return ResponseEntity.ok(appUserDTO);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PatchMapping("/invitation/{invitationSenderID}/{invitationReceiverID}")
    public ResponseEntity<String> inviteFriend(@PathVariable("invitationSenderID") Long invitationSenderID,
                               @PathVariable("invitationReceiverID") Long invitationReceiverID){
        try {
            String result = appUserService.askForFriendship(invitationSenderID, invitationReceiverID);
            return ResponseEntity.ok(result);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/acceptInvitation/{invitationReceiverID}/{invitationSenderID}")
    public ResponseEntity<String> acceptInvitation(@PathVariable("invitationReceiverID") Long invitationReceiverID,
                                   @PathVariable("invitationSenderID") Long invitationSenderID){
        try {
            String result = appUserService.acceptFriendship(invitationReceiverID, invitationSenderID);
            return ResponseEntity.ok(result);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

