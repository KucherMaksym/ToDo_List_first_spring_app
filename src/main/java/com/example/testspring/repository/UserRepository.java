package com.example.testspring.repository;

import com.example.testspring.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findAppUserByUsername(String username);
    Boolean existsByUsername(String username);


    List<AppUser> findAll();
    AppUser save(AppUser appUser);

    void deleteById(long userId);

}
