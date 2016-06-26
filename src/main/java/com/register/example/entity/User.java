package com.register.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String login;

    @Column(unique=true)
    private String email;

    private String name;

    private String lastName;

    private String passwordHash;


    public User(String name, String lastName, String email, String login, String passwordHash) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.passwordHash = passwordHash;
    }
}
