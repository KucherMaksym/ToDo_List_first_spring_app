package com.example.testspring.model;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;

@Data
@Entity
@Table
public class AppUser {

    @Id
    @GeneratedValue
    public Long id;
    private String username;
    private String password;
    private LocalDate birthDate;
    @Column(unique = true)
    private String email;
    @Transient
    private int age;

}
