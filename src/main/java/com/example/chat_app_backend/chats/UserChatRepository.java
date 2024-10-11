package com.example.chat_app_backend.chats;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    @Query("select case when count (fd) > 0" +
            "then true else false end from " +
            "AppUser u join u.friends fd where " +
            "u.id = ?1 and fd.id = ?2 and u.id <> fd.id")
    boolean friendship(Long userID, Long friendID);

    @Query("select c from UserChat c where c.sender.id = ?1 " +
            "and c.receiver.id = ?2 or (c.sender.id = ?2 and c.receiver.id = ?1)")
    List<UserChat> conversation(Long senderID, Long receiverID);
}
