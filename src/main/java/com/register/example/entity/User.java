package com.register.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String login;

    @Column(unique = true)
    private String email;

    private String name;

    private String lastName;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role=Role.USER;

    private Boolean enabled = false;

    private Boolean accountNonExpired = true;

    private Boolean credentialsNonExpired = true;

    private Boolean accountNonLocked = true;

    private LocalDateTime lastSuccesfullLogin=null;

    private LocalDate registrationDate=LocalDate.now();



    public User(String name, String lastName, String email, String login, String passwordHash,Role role,Boolean enabled) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.enabled=enabled;
    }
}
