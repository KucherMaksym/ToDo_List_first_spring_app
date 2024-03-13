package com.example.testspring.controller;


import com.example.testspring.model.AppUser;
import com.example.testspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")//разрешает делать запрос только с этого сайта
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    private AppUser createUser(@RequestBody AppUser appUser) {
        AppUser newUser = userService.createUser(appUser);
        return newUser;
    }

    @GetMapping("/all")
    private List<AppUser> getAllUsers() {
        List<AppUser> allUsers = userService.getAllUsers();
        return allUsers;
    }

    @DeleteMapping("/delete/{userId}")
    private void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }


}
