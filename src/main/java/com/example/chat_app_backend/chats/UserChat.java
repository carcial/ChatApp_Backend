package com.example.chat_app_backend.chats;

import com.example.chat_app_backend.appUser.AppUser;
import com.example.chat_app_backend.messages.Messages;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender", nullable = false)
    private AppUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", nullable = false)
    private AppUser receiver;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
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
