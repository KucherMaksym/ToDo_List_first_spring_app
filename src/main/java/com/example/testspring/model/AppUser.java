package com.example.testspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class AppUser {

    @Id
    @GeneratedValue
    public Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private LocalDate birthDate;
    @Column(unique = true)
    private String email;
    @Transient
    private int age;
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonIgnore // Игнорировать это поле при сериализации,
    // иначе будет возвращать огромный повторяющийся json
    private List<Task> userTasks = new ArrayList<Task>();

}
