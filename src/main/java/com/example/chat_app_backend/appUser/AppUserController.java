package com.example.chat_app_backend.appUser;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class AppUserController {

    private final AppUserService appUserService;


    @PostMapping("/register")
    public String register(@RequestBody AppUser appUser){
        appUserService.addUser(appUser);

        return "Registration Successful";
    }

    @GetMapping("/login")
    public AppUser login(@RequestBody AppUser appUser){
        return appUserService.login(appUser);
    }
}
