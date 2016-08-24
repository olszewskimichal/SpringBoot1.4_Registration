package com.register.example.repository;

import com.register.example.entity.User;
import com.register.example.entity.tokens.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findVerificationTokenByToken(String token);

    Optional<VerificationToken> findVerificationTokenByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from VerificationToken u where u.expiryDate < ?1 or u.isUsed=TRUE ")
    Integer deleteVerificationTokenByExpiryDateLessThen(LocalDateTime date);
}
