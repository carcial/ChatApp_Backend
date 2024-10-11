package com.example.chat_app_backend.chats;

import com.example.chat_app_backend.appUser.AppUser;
import com.example.chat_app_backend.messages.Messages;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private AppUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    private AppUser receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "messageID")
    private Messages message;


    @Override
    public String toString() {
        return "UserChat{" +
                "sender=" + sender.getUserName()+
                ", receiver=" + receiver.getUserName() +
                ", message=" + message.getMessage() +
                '}';
    }
}
