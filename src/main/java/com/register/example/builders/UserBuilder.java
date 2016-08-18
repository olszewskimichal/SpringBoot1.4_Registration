package com.register.example.builders;

import com.register.example.entity.Role;
import com.register.example.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserBuilder {
    private String name = "przykladoweImie";
    private String lastName = "przykladoweNazwisko";
    private String email;
    private String login;
    private String passwordHash;
    private Role role=Role.USER;
    private Boolean enabled=false;

    public UserBuilder(String email, String login) {
        this.email = email;
        this.login = login;
    }

    public UserBuilder withPassword(String password) {
        this.passwordHash = new BCryptPasswordEncoder().encode(password);
        return this;
    }

    public UserBuilder withRole(Role role){
        this.role=role;
        return this;
    }

    public UserBuilder withEnabled(Boolean enabled){
        this.enabled=enabled;
        return this;
    }

    public User build() {
        return new User(name, lastName, email, login, passwordHash,role,enabled);
    }

}
