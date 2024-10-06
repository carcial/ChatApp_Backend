package com.example.chat_app_backend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Trying {

    @RequestMapping("/")
    public String greet(){
        return  "Welcome to ChAPP";
    }
}
