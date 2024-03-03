package com.example.testspring.repository;

import com.example.testspring.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findAppUserByUsername(String username);

    List<AppUser> findAll();
    AppUser save(AppUser appUser);

    void deleteById(long userId);

}
