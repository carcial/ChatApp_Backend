package com.example.chat_app_backend.appUser;

import com.example.chat_app_backend.appUser.dtos.AppUserDTO;
import com.example.chat_app_backend.appUser.dtos.InvitationDTO;
import com.example.chat_app_backend.appUser.dtos.SearchedAppUserDTO;
import com.example.chat_app_backend.images.Images;
import com.example.chat_app_backend.images.ImagesRepository;
import com.example.chat_app_backend.images.ImagesService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class AppUserService {

    private final AppUserRepository appUserRepository;

    private final ImagesRepository imagesRepository;

    private final ImagesService imagesService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SimpMessageSendingOperations messagingTemplate;






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

    /*
    @Transactional
    public String deleteAppUser(Long id){
        AppUser appUser = appUserRepository.findById(id).orElseThrow(() ->
                {throw new IllegalStateException("There is no User with ID: "+ id);});
        appUserRepository.delete(appUser);
        return  appUser.getUserName()+" was deleted successfully";
    }*/



    @Transactional
    public String deleteAppUser(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with ID " + id + " does not exist"));

        // Remove all associations
        // Remove from friends
        for (AppUser friend : appUser.getFriends()) {
            friend.getFriends().remove(appUser);
        }
        appUser.getFriends().clear();

        // Remove sent and received friend invitations
        appUser.getSendFriendInvitationTO().clear();
        appUser.getReceiveFriendInvitationFROM().clear();

        // Remove chat associations
        appUser.getSender().clear();
        appUser.getReceiver().clear();

        // Finally delete the user
        appUserRepository.delete(appUser);
        return appUser.getUserName() + " was deleted successfully";
    }

    /*
    @Transactional
    public String deleteUserAndDependencies(Long userId) {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with ID " + userId + " not found"));

        // Delete associated UserChats where the user is the sender or receiver
        appUserRepository.deleteUserChatsByUserId(userId);

        // Delete friend references from other users to this user
        appUserRepository.deleteFriendReferences(userId);

        // Delete sent invitations
        appUserRepository.deleteSentInvitations(userId);

        // Delete received invitations
        appUserRepository.deleteReceivedInvitations(userId);

        // Finally, delete the user
        appUserRepository.delete(appUser);

        return appUser.getUserName() + " was deleted successfully along with associated data";
    }*/


    @Transactional
    public AppUserDTO updateAppUser(Long id, String newUserName, String newEmail, String newPassword){

        AppUser appUser = appUserRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("There is user with ID: "+ id));

        if (newUserName != null && !newUserName.isEmpty() && !newUserName.equals(appUser.getUserName())) {
            appUser.setUserName(newUserName);
        }
        if (newEmail != null && !newEmail.isEmpty() && !newEmail.equals(appUser.getEmail())) {
            appUser.setEmail(newEmail);
        }
        if (newPassword != null && !newPassword.isEmpty() && !bCryptPasswordEncoder.matches(newPassword, appUser.getPassword())) {
            appUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        }

        appUserRepository.save(appUser);

        return convertToDTO(appUser);
    }


    public AppUserDTO convertToDTO(AppUser appUser){
        Long profilePic_imageId = 0L;
        if(appUser.getProfilePic() != null){
            profilePic_imageId = appUser.getProfilePic().getImageID();
        }
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
                profilePic_imageId,
                appUser.getUserName(),
                appUser.getEmail(),
                friendsDTO,
                sendFriendInvitationDTO,
                receiveFriendInvitationDTO);
    }

    public SearchedAppUserDTO convertAppUserTo_DTO(AppUser appUser){
        return new SearchedAppUserDTO(appUser.getId(), appUser.getUserName(), appUser.getEmail());
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

        if(invitationSender.getFriends().contains(invitationReceiver)){
            throw new IllegalStateException("The Two users are already friends");
        }

        invitationSender.inviteFriend(invitationReceiver);
        invitationReceiver.getReceiveFriendInvitationFROM().add(invitationSender);

        appUserRepository.save(invitationSender);
        appUserRepository.save(invitationReceiver);


        InvitationDTO invitationDTO = new InvitationDTO(convertAppUserTo_DTO(invitationSender),
                convertAppUserTo_DTO(invitationReceiver));

        messagingTemplate.convertAndSendToUser(invitationReceiverID.toString(),
                "/start/invite",
                invitationDTO);
        messagingTemplate.convertAndSendToUser(invitationSenderID.toString(),
                "/start/invite",
                invitationDTO);

        System.out.println("Invitation: "+ invitationDTO);

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

        invitationReceiver.getReceiveFriendInvitationFROM().remove(invitationSender);
        invitationSender.getSendFriendInvitationTO().remove(invitationReceiver);

        appUserRepository.save(invitationSender);
        appUserRepository.save(invitationReceiver);


        Long[] usersIDs = {invitationSenderID, invitationReceiverID};

        messagingTemplate.convertAndSendToUser(invitationReceiverID.toString(),
                "/start/accept",
                usersIDs);
        messagingTemplate.convertAndSendToUser(invitationSenderID.toString(),
                "/start/accept",
                usersIDs);

        return "accepted";
    }

    public String refuseInvitation(Long invitationSenderID, Long invitationReceiverID){
        AppUser invitationSender = appUserRepository.findById(invitationSenderID).orElseThrow(() ->{
            throw new IllegalStateException("There is no User with ID: "+ invitationSenderID);
        });
        AppUser invitationReceiver = appUserRepository.findById(invitationReceiverID).orElseThrow(()->{
            throw new IllegalStateException("There is no User with ID: "+ invitationReceiverID);
        });

        if(! invitationReceiver.getReceiveFriendInvitationFROM().contains(invitationSender)){
            throw new IllegalStateException("No invitation was send from user: "+ invitationSender.getUserName());
        }

        invitationReceiver.refuseInvitation(invitationSender);
        invitationSender.getSendFriendInvitationTO().remove(invitationReceiver);

        appUserRepository.save(invitationReceiver);
        appUserRepository.save(invitationSender);

        Long[] usersIDs = {invitationSenderID, invitationReceiverID};

        messagingTemplate.convertAndSendToUser(invitationReceiverID.toString(),
                "/start/refuse",
                usersIDs);
        messagingTemplate.convertAndSendToUser(invitationSenderID.toString(),
                "/start/refuse",
                usersIDs);

        System.out.println("UsersIDs: "+ Arrays.toString(usersIDs));

        return "Invitation refused";
    }


    public List<SearchedAppUserDTO> getSearchedAppUser(Long userId){
        boolean exist = appUserRepository.existsById(userId);

        if(!exist){
            throw new IllegalStateException("This user does not exist");
        }

        return appUserRepository.findAllOtherUsers(userId)
                .stream()
                .map(this::convertAppUserTo_DTO)
                .collect(Collectors.toList());
    }

    public Images getUploadedImage(Long imageId){
        boolean exist = imagesRepository.existsById(imageId);
        if(!exist){
            throw new IllegalStateException("No image was found with the ID: "+ imageId);
        }
        Images images = imagesRepository.getOneImageById(imageId);

        return new Images(imagesService.decompressImage(images.getImage()),
                images.getImageContentType());
    }
}
