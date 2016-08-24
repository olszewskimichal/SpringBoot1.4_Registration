package com.register.example.repository;

import com.register.example.entity.tokens.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CustomPersistentTokenRepository extends JpaRepository<PersistentLogin, String> {
    @Transactional
    @Modifying
    Integer deletePersistentLoginByUsername(String username);
}
