package com.example.testspring.controller;


import com.example.testspring.MyUserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/secured")
public class MainController {

    @GetMapping("/no")
    public String sayHelelo(Principal principal) {
        if (principal == null) {
            return "user not found";
        } else {
            return principal.getName();
        }
    }


    @GetMapping("/yes")
    public String sayHello() {
        return "Hello";
    }

}
