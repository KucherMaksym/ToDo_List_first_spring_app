package com.example.testspring.controller;


import com.example.testspring.MyUserPrincipal;
import com.example.testspring.model.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/secured")
public class MainController {

    @GetMapping("/no")
    public String getUserByJwt(Principal principal) {
        if (principal == null) {
            return "user not found";
        } else {
            return getOnlyName(principal);
        }
    }

    @GetMapping("/yes")
    public String sayHello() {
        return "Hello" + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getOnlyName(Principal principal) {
        int startIndex = principal.getName().indexOf("username=") + "username=".length();
        int endIndex = principal.getName().indexOf(",", startIndex);
        return principal.getName().substring(startIndex, endIndex);
    }

}
