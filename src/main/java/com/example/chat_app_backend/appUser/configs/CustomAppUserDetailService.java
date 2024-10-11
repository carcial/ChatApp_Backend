package com.example.chat_app_backend.appUser.configs;

import com.example.chat_app_backend.appUser.AppUser;
import com.example.chat_app_backend.appUser.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@NoArgsConstructor
@Service
public class CustomAppUserDetailService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findAppUserByEmail(email);
        System.out.println("Loading user with email: " + email);
        if (appUser == null){
            throw new UsernameNotFoundException("This user was not found");
        }
        System.out.println("User loaded: " + appUser.getEmail());
        return new CustomAppUserDetail(appUser);
    }
}
