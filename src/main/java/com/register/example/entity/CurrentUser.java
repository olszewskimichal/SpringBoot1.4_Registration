package com.register.example.entity;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.AuthorityUtils;

@EqualsAndHashCode(callSuper = true)
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    transient User user;

    public CurrentUser(User user) {
        super(user.getLogin(), user.getPasswordHash(), user.getEnabled(), user.getAccountNonExpired(),
                user.getCredentialsNonExpired(), user.getAccountNonLocked(),
                AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }


    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                '}';
    }
}