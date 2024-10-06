package com.example.chat_app_backend.messages;

import com.example.chat_app_backend.chats.UserChat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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
    @Column(nullable = false, columnDefinition = "Text")
    private String message;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "message")
    private List<UserChat> allChats;

    @Column(nullable = false)
    private Date sendingTime = Date.valueOf(LocalDate.now());

    public Messages(String message) {
        this.message = message;
    }
}
