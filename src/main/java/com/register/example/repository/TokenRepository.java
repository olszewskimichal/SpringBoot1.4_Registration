package com.register.example.repository;

import com.register.example.entity.User;
import com.register.example.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findVerificationTokenByToken(String token);

    Optional<VerificationToken> findVerificationTokenByUser(User user);
}
