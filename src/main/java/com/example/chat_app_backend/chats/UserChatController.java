package com.example.chat_app_backend.chats;


import com.example.chat_app_backend.chats.dtos.UserChatDTO;
import com.example.chat_app_backend.images.Images;
import com.example.chat_app_backend.messages.Messages;
import com.example.chat_app_backend.messages.MessagesDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/chats")
public class UserChatController {

    private final UserChatService userChatService;





    @PostMapping("/sendMessage/{senderID}/{receiverID}")
    public ResponseEntity<String> sendMessages(@PathVariable("senderID") Long senderID,
                                               @PathVariable("receiverID") Long receiverID,
                                               @RequestPart(value = "message", required = false) String messageToSend,
                                               @RequestPart(name="file", required = false)MultipartFile file){
        try {
            String result = userChatService.sendMessage(senderID, receiverID, messageToSend, file);

            return ResponseEntity.ok(result);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error sending message: "+e.getMessage());
        }
    }


    @DeleteMapping("/deleteChat/{chatID}")
    public ResponseEntity<String> deleteChat(@PathVariable Long chatID){
        try {
            String result = userChatService.deleteUserChat(chatID);
            return ResponseEntity.ok(result);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/conversation/{senderID}/{receiverID}")
    public ResponseEntity<List<UserChatDTO>> getConversation(@PathVariable("senderID") Long senderID,
                                                             @PathVariable("receiverID") Long receiverID){
        try {
            List<UserChatDTO> result = userChatService.getConversation(senderID, receiverID);
            return ResponseEntity.ok(result);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
