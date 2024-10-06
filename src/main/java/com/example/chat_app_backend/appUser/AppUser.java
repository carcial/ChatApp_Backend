package com.example.chat_app_backend.appUser;

import com.example.chat_app_backend.chats.UserChat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
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

    @Lob
    @Column(name = "profile_pic",columnDefinition = "Text")
    private String profilePic;

    @OneToMany(mappedBy = "sender")
    private Collection<UserChat> sender;

    @OneToMany(mappedBy = "receiver")
    private Collection<UserChat> receiver;

    @ManyToMany
    @JoinTable(name = "AllFriends", joinColumns = {@JoinColumn(name = "UserID")},
                                    inverseJoinColumns = {@JoinColumn(name = "FriendID")})
    private Set<AppUser> friends;

    @ManyToMany
    @JoinTable(name = "FriendshipInvitation", joinColumns = {@JoinColumn(name = "SenderOfFriendship")},
                                              inverseJoinColumns = {@JoinColumn(name = "ReceiverOfFriendship")})
    private Set<AppUser> friendsInvitation;

    public AppUser(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public AppUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void inviteFriend(AppUser friend){
        this.friendsInvitation.add(friend);
    }

    public void acceptInvitation(AppUser friend){
        this.friends.add(friend);
    }
}
