package com.example.testspring.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table
public class Task {


    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDate dateCreation;
    private boolean isCompleted;
    @ManyToOne
    private AppUser appUser;



}