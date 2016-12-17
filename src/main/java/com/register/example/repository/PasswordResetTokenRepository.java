package com.register.example.repository;

import com.register.example.entity.User;
import com.register.example.entity.tokens.PasswordResetToken;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Profile("!test")
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findPasswordResetTokenByTokenString(String token);

    Optional<PasswordResetToken> findPasswordResetTokenByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from PasswordResetToken u where u.isUsed=TRUE ")
    Integer deletePasswordResetTokenWhenIsUsed();
}
