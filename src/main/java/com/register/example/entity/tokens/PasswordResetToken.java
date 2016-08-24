package com.register.example.entity.tokens;

import com.register.example.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class PasswordResetToken extends Token {

    public PasswordResetToken(String token, User user, Boolean isUsed) {
        super(token,user,isUsed);
    }

}
