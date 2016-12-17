package com.register.example.entity.tokens;

import com.register.example.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
public abstract class Token {
    @Id
    @GeneratedValue
    private Long id;

    private String tokenString;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Boolean isUsed = false;

    public Token(String token, User user, Boolean isUsed) {
        this.tokenString = token;
        this.user = user;
        this.isUsed = isUsed;
    }
}
