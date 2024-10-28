package com.example.chat_app_backend.messages;

import com.example.chat_app_backend.images.ImageDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class MessagesDTO {

    private Long messageId;
    private Long senderId;
    private Long imageId;
    private String message;
    //private ImageDTO image; // Instead of returning the entire file

    @DateTimeFormat(pattern = "HH:mm   dd.MM.yyyy")
    @JsonFormat(pattern = "HH:mm   dd.MM.yyyy")
    private LocalDateTime sendingTime;

}
