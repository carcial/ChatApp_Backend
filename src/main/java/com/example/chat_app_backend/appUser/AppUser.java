package com.example.chat_app_backend.appUser;

import com.example.chat_app_backend.chats.UserChat;
import com.example.chat_app_backend.images.Images;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String email;
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    private Images profilePic;

    @OneToMany(mappedBy = "sender")
    private Collection<UserChat> sender;

    @OneToMany(mappedBy = "receiver")
    private Collection<UserChat> receiver;

    @ManyToMany
    @JoinTable(name = "AllFriends", joinColumns = {@JoinColumn(name = "UserID")},
                                    inverseJoinColumns = {@JoinColumn(name = "FriendID")})
    private Set<AppUser> friends;


    @ManyToMany
    @JoinTable(name = "FriendshipInvitationTO", joinColumns = {@JoinColumn(name = "SenderOfFriendship")},
                                              inverseJoinColumns = {@JoinColumn(name = "ReceiverOfFriendship")})
    private Set<AppUser> sendFriendInvitationTO;

    @ManyToMany
    @JoinTable(name = "FriendshipInvitationFROM", joinColumns = {@JoinColumn(name = "ReceiverOfFriendship")},
                                             inverseJoinColumns = {@JoinColumn(name = "SenderOfFriendship")})
    private Set<AppUser> receiveFriendInvitationFROM;


    //This user can invite another user(friend)
    public void inviteFriend(AppUser friend){
        this.sendFriendInvitationTO.add(friend);
    }

    //This user can accept the invitation of another user(friend)
    public void acceptInvitation(AppUser friend){
        this.friends.add(friend);
    }

    public void refuseInvitation(AppUser senderOfInvitation){
        this.receiveFriendInvitationFROM.remove(senderOfInvitation);
    }

}
