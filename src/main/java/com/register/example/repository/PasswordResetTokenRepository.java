package com.register.example.repository;

import com.register.example.entity.User;
import com.register.example.entity.tokens.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findPasswordResetTokenByToken(String token);

    Optional<PasswordResetToken> findPasswordResetTokenByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from PasswordResetToken u where u.isUsed=TRUE ")
    Integer deletePasswordResetTokenWhenIsUsed();
}
