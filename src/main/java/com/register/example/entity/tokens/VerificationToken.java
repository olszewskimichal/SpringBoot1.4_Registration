package com.register.example.entity.tokens;

import com.register.example.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken extends Token {

    private LocalDateTime expiryDate=LocalDateTime.now().plusDays(1);


    public VerificationToken(String token,User user,LocalDateTime date,Boolean isUsed) {
        super(token,user,isUsed);
        this.expiryDate=date;
    }

}
