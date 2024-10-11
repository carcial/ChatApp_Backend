package com.example.chat_app_backend.appUser;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;






    public void addUser(AppUser appUser){

        boolean exist = appUserRepository.findUserByEmail(appUser.getEmail());

        if(exist){
            throw new IllegalArgumentException("This email has already been taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
    }

    public AppUserDTO login(AppUser appUser){
        AppUser storedUser = appUserRepository.findAppUserByEmail(appUser.getEmail());
        if (storedUser != null && bCryptPasswordEncoder.matches(appUser.getPassword(), storedUser.getPassword())) {
            // Password matches, return DTO
            return convertToDTO(storedUser);
        }
        throw new BadCredentialsException("Invalid email or password");
    }

    public AppUserDTO convertToDTO(AppUser appUser){
        Set<AppUserDTO> sendFriendInvitationDTO = appUser.getSendFriendInvitationTO()
                .stream()
                .map(invitedFriends -> new AppUserDTO(invitedFriends.getId(),
                        invitedFriends.getUserName(),
                        invitedFriends.getEmail()))
                .collect(Collectors.toSet());
        Set<AppUserDTO> receiveFriendInvitationDTO = appUser.getReceiveFriendInvitationFROM()
                .stream()
                .map(invitedFriends -> new AppUserDTO(invitedFriends.getId(),
                        invitedFriends.getUserName(),
                        invitedFriends.getEmail()))
                .collect(Collectors.toSet());
        Set<AppUserDTO> friendsDTO = appUser.getFriends()
                .stream()
                .map(friend -> new AppUserDTO(friend.getId(),
                        friend.getUserName(),
                        friend.getEmail()))
                .collect(Collectors.toSet());
        return new AppUserDTO(appUser.getId(),
                appUser.getUserName(),
                appUser.getEmail(),
                friendsDTO,
                sendFriendInvitationDTO,
                receiveFriendInvitationDTO);
    }

    public String askForFriendship(Long invitationSenderID, Long invitationReceiverID){
        if(Objects.equals(invitationReceiverID, invitationSenderID)){
            throw new IllegalStateException("A user cannot be friends with itself");
        }
        AppUser invitationSender = appUserRepository.findById(invitationSenderID).orElseThrow(() ->{
            throw new IllegalStateException("There is no User with ID: "+ invitationSenderID);
        });
        AppUser invitationReceiver = appUserRepository.findById(invitationReceiverID).orElseThrow(()->{
            throw new IllegalStateException("There is no User with ID: "+ invitationReceiverID);
        });

        invitationSender.inviteFriend(invitationReceiver);
        invitationReceiver.getReceiveFriendInvitationFROM().add(invitationSender);

        appUserRepository.save(invitationSender);
        appUserRepository.save(invitationReceiver);

        return "invited";
    }

    public String acceptFriendship(Long invitationReceiverID, Long invitationSenderID){
        AppUser invitationSender = appUserRepository.findById(invitationSenderID).orElseThrow(() ->{
            throw new IllegalStateException("There is no User with ID: "+ invitationSenderID);
        });
        AppUser invitationReceiver = appUserRepository.findById(invitationReceiverID).orElseThrow(()->{
            throw new IllegalStateException("There is no User with ID: "+ invitationReceiverID);
        });

        if(! invitationReceiver.getReceiveFriendInvitationFROM().contains(invitationSender)){
            throw new IllegalStateException("No invitation was send from user: "+ invitationSender.getUserName());
        }

        invitationReceiver.acceptInvitation(invitationSender);// the receiver of the invitation accepts the invitation
        invitationSender.acceptInvitation(invitationReceiver);// and the sender should now also automatically accept
        // the friendship since the invitation was sent by him/her


        appUserRepository.save(invitationSender);
        appUserRepository.save(invitationReceiver);

        return "accepted";
    }
}
