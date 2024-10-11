package com.example.chat_app_backend.messages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Messages, Long> {

    @Query("select m from Messages m where m.messageId = ?1")
    Messages findMessagesByID(Long messageID);
}
