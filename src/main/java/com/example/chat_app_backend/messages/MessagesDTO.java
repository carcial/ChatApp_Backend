package com.example.chat_app_backend.messages;

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
    private String message;
    private String fileDownloadLink; // Instead of returning the entire file
    private LocalDateTime sendingTime;
}
