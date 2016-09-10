package com.register.example.builders;

import com.register.example.entity.User;
import com.register.example.entity.tokens.PasswordResetToken;

import java.util.UUID;

public class PasswordResetTokenBuilder {
    private String token = UUID.randomUUID().toString();
    private User user;
    private Boolean isUsed;

    public PasswordResetTokenBuilder(User user, Boolean isUsed) {
        this.user = user;
        this.isUsed = isUsed;
    }


    public PasswordResetToken build() {
        return new PasswordResetToken(token, user, isUsed);
    }

}
