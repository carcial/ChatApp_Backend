package com.example.chat_app_backend.appUser;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;






    public void addUser(AppUser appUser){

        boolean exist = appUserRepository.findUserByEmail(appUser.getEmail());

        if(exist){
            throw new IllegalStateException("This email has already been taken");
        }

        // still need to encode the password

        appUserRepository.save(appUser);
    }

    public AppUser login(AppUser appUser){
        boolean exist = appUserRepository.findUserByEmail(appUser.getEmail());
        if(!exist){
            throw new IllegalStateException("Email not found");
        }
        return appUserRepository.findAppUserByEmail(appUser.getEmail());
    }

    public String askForFriendship(Long invitationSenderID, Long invitationReceiverID){
        AppUser invitationSender = appUserRepository.findById(invitationSenderID).orElseThrow(() ->{
            throw new IllegalStateException("There is no User with ID: "+ invitationSenderID);
        });
        AppUser invitationReceiver = appUserRepository.findById(invitationReceiverID).orElseThrow(()->{
            throw new IllegalStateException("There is no User with ID: "+ invitationReceiverID);
        });

        invitationSender.inviteFriend(invitationReceiver);

        return "invited";
    }

    public String acceptFriendship(Long invitationReceiverID, Long invitationSenderID){
        AppUser invitationSender = appUserRepository.findById(invitationSenderID).orElseThrow(() ->{
            throw new IllegalStateException("There is no User with ID: "+ invitationSenderID);
        });
        AppUser invitationReceiver = appUserRepository.findById(invitationReceiverID).orElseThrow(()->{
            throw new IllegalStateException("There is no User with ID: "+ invitationReceiverID);
        });

        invitationReceiver.acceptInvitation(invitationSender);
        invitationSender.acceptInvitation(invitationReceiver);

        return "accepted";
    }
}
