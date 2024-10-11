package com.example.chat_app_backend.messages;


import com.example.chat_app_backend.images.Images;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;



import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Lob
    @Column(columnDefinition = "Text")
    private String message;

    @OneToOne(fetch = FetchType.EAGER)
    private Images images;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm dd.MM.yyyy")
    @JsonFormat(pattern = "HH:mm dd.MM.yyyy")
    private LocalDateTime sendingTime = LocalDateTime.now();



    public Messages(String message) {
        this.message = message;
    }

    public Messages(Images images) {
        this.images = images;
    }

    public Messages(String message, Images images) {
        this.message = message;
        this.images = images;
    }



    @Override
    public String toString() {
        return "Messages{" +
                "message='" + message + '\'' +
                ", sendingTime=" + sendingTime +
                '}';
    }
}
