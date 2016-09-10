package com.register.example.builders;

import com.register.example.entity.User;
import com.register.example.entity.tokens.VerificationToken;

import java.time.LocalDateTime;
import java.util.UUID;

public class VerificationTokenBuilder {
    private String token = UUID.randomUUID().toString();
    private User user;
    private LocalDateTime date = LocalDateTime.now();
    private Boolean isUsed;

    public VerificationTokenBuilder(User user, Boolean isUsed) {
        this.user = user;
        this.isUsed = isUsed;
    }

    public VerificationTokenBuilder withDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public VerificationToken build() {
        return new VerificationToken(token, user, date, isUsed);
    }

}
