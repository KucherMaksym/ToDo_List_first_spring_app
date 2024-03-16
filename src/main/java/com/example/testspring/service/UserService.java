package com.example.testspring.service;


import com.example.testspring.MyUserPrincipal;
import com.example.testspring.config.SecurityConfig;
import com.example.testspring.model.AppUser;
import com.example.testspring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    public final SecurityConfig securityConfig;

    @Autowired
    public UserService(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }
    @Override//метод для поиска пользователей в БД и и позволяет им входить в аккац
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findAppUserByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return MyUserPrincipal.build(appUser);
    }

    @Transactional
    public boolean authenticateUser(String username, String password) {
        AppUser user = userRepository.findAppUserByUsername(username);
        if (user != null) {
            // Проверяем, что введенный пароль совпадает с сохраненным в базе данных
            if (securityConfig.passwordEncoder().matches(password, user.getPassword())) {
                return true; // Аутентификация успешна
            }
        }
        return false; // Аутентификация не удалась
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
