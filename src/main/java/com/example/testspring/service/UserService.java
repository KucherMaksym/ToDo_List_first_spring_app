package com.example.testspring.service;


import com.example.testspring.model.AppUser;
import com.example.testspring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public AppUser createUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    @Transactional
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public AppUser getUserByUsername(String username) {
        return userRepository.findAppUserByUsername(username);
    }

    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

}
