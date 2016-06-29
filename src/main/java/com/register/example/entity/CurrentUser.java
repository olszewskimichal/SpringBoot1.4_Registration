package com.register.example.entity;

import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getLogin(), user.getPasswordHash(), true, true, true, true, AuthorityUtils.NO_AUTHORITIES);
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