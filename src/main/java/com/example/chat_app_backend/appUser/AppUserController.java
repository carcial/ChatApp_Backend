package com.example.chat_app_backend.appUser;

import com.example.chat_app_backend.appUser.dtos.AppUserDTO;
import com.example.chat_app_backend.appUser.dtos.SearchedAppUserDTO;
import com.example.chat_app_backend.images.Images;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/deleteUser/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userID){
        try {
            String result = appUserService.deleteAppUser(userID);
            //String result = appUserService.deleteUserAndDependencies(userID);
            return ResponseEntity.ok(result);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/updateUserInfos/{id}")
    public ResponseEntity<AppUserDTO> updateUser(@PathVariable Long id,
                                                 @RequestParam(required = false) String newUserName,
                                                 @RequestParam(required = false) String newEmail,
                                                 @RequestParam(required = false) String newPassword){

        try {
            AppUserDTO appUserDTO = appUserService.updateAppUser(id, newUserName, newEmail, newPassword);
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

    @PatchMapping("/acceptInvitation/{invitationReceiverID}/{invitationSenderID}")
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

    @PatchMapping("/refuseInvitation/{invitationSenderID}/{invitationReceiverID}")
    public ResponseEntity<String> refuseInvitation(@PathVariable Long invitationSenderID,
                                                   @PathVariable Long invitationReceiverID){
        try {
            String result = appUserService.refuseInvitation(invitationSenderID, invitationReceiverID);
            return ResponseEntity.ok(result);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("seeOtherUsers/{userID}")
    public ResponseEntity<List<SearchedAppUserDTO>> getSearchedUsers(@PathVariable Long userID){
        try {
            List<SearchedAppUserDTO> result = appUserService.getSearchedAppUser(userID);
            return ResponseEntity.ok(result);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("getAnImage/{imageId}")
    public ResponseEntity<?> getUploadedImage(@PathVariable Long imageId){
       try {
           Images images = appUserService.getUploadedImage(imageId);
           return ResponseEntity.status(HttpStatus.OK)
                   .contentType(MediaType.valueOf(images.getImageContentType()))
                   .body(images.getImage());
       }
       catch (IllegalStateException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }

    }


}

