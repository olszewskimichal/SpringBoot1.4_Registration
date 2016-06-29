package com.register.example.builders;

import com.register.example.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserBuilder {
    private String name = "przykladoweImie";
    private String lastName = "przykladoweNazwisko";
    private String email;
    private String login;
    private String passwordHash;

    public UserBuilder(String email, String login) {
        this.email = email;
        this.login = login;
    }

    public UserBuilder withPassword(String password) {
        this.passwordHash = new BCryptPasswordEncoder().encode(password);
        return this;
    }

    public User build() {
        return new User(name, lastName, email, login, passwordHash);
    }

}
