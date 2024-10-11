package com.example.chat_app_backend.chats;

import com.example.chat_app_backend.appUser.AppUser;
import com.example.chat_app_backend.appUser.AppUserRepository;
import com.example.chat_app_backend.chats.dtos.AppUserChatDTO;
import com.example.chat_app_backend.chats.dtos.UserChatDTO;
import com.example.chat_app_backend.images.Images;
import com.example.chat_app_backend.images.ImagesService;
import com.example.chat_app_backend.messages.MessageRepository;
import com.example.chat_app_backend.messages.Messages;
import com.example.chat_app_backend.messages.MessagesDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserChatService {

    private final UserChatRepository userChatRepository;
    private final MessageRepository messageRepository;
    private final AppUserRepository appUserRepository;

    private final ImagesService imagesService;




    public AppUserChatDTO convertAppUserTo_DTO(AppUser appUser){
        return new AppUserChatDTO(appUser.getId(), appUser.getUserName());
    }

    public MultipartFile byteArrayToMultipartFile(byte[] imageBytes, String contentType){
        byte[] decompressedImageBytes  = imagesService.decompressImage(imageBytes);
        return new MockMultipartFile("file", "file", contentType, decompressedImageBytes);
    }

    public MessagesDTO convertMessagesTo_DTO(Messages messages){


        return new MessagesDTO(messages.getMessageId(),
                               messages.getMessage(),
                               messages.getMessage(),
                               messages.getSendingTime());
    }

    public UserChatDTO convertUserChatTo_DTO(UserChat userChat){
        return new UserChatDTO(userChat.getChatId(),
                convertAppUserTo_DTO(userChat.getSender()),
                convertAppUserTo_DTO(userChat.getReceiver()),
                userChat.getMessage());
    }


    public String sendMessage(Long senderID, Long receiverID, String message, MultipartFile file) throws IOException {

        boolean areFriends = userChatRepository.friendship(senderID, receiverID);

        if(!areFriends){
            throw new IllegalStateException("These users are not friends");
        }
        if(message == null & file.isEmpty()){
            throw new IllegalStateException("There is no message and no file");
        }



        AppUser messageSender = appUserRepository.findById(senderID).orElseThrow(()->{
            throw new IllegalStateException("There is no User with ID: "+ senderID);
        });
        AppUser messageReceiver = appUserRepository.findById(receiverID).orElseThrow(()-> {
            throw new IllegalStateException("There is no User with ID: " + receiverID);
        });

        UserChat userChat = new UserChat();

        Messages messages = new Messages(message);
        if(!file.isEmpty()){
            Images images = new Images(file.getBytes(), file.getContentType());
            messages.setImages(images);
            messages = imagesService.compressImagesInMessages(messages);
        }

        userChat.setSender(messageSender);
        userChat.setReceiver(messageReceiver);
        userChat.setMessage(messages);


        messageRepository.save(messages);
        userChatRepository.save(userChat);

        return "message send successfully ";
    }

    public List<UserChatDTO> getConversation(Long senderID, Long receiverID){
        boolean areFriends = userChatRepository.friendship(senderID, receiverID);

        if(!areFriends){
            throw new IllegalStateException("These users are not friends");
        }
        return userChatRepository.conversation(senderID, receiverID)
                .stream()
                .map(uc -> {
                    if (uc.getMessage().getImages() != null && uc.getMessage().getImages().getImage() != null) {
                        uc.getMessage().getImages().setImage(
                                imagesService.decompressImage(uc.getMessage().getImages().getImage())
                        );
                    }
                    return convertUserChatTo_DTO(uc);
                })
                .collect(Collectors.toList());
    }
}
