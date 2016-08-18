package com.register.example.service;

import com.register.example.entity.User;
import com.register.example.entity.VerificationToken;
import com.register.example.forms.UserCreateForm;
import com.register.example.repository.TokenRepository;
import com.register.example.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    @Modifying
    public User create(UserCreateForm form) {
        log.info("tworzenie uzytkownika =" + form.getLogin());
        User user = new User();
        user.setEmail(form.getEmail());
        user.setLastName(form.getLastName());
        user.setLogin(form.getLogin());
        user.setName(form.getName());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        user = userRepository.save(user);
        createVerificationToken(user);
        log.info("Stworzono uzytkownika o id=" + user.getId());
        return user;
    }
    public void createVerificationToken(User user){
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);
        log.info("Stworzono token dla uzytkownika o id=" + user.getId());
    }

    @Transactional
    @Modifying
    public void delete(User user) {
        log.info("usuwanie uzytkownika o id=" + user.getId());
        Optional<VerificationToken> verificationToken=getVerificationToken(user);
        if (verificationToken.isPresent()) {
            tokenRepository.delete(verificationToken.get());
        }
        userRepository.delete(user.getId());
    }


    public Optional<VerificationToken> getVerificationToken(String token) {
        log.info("Pobieranie tokena {}", token);
        return tokenRepository.findVerificationTokenByToken(token);
    }

    public Optional<VerificationToken> getVerificationToken(User user) {
        log.info("Pobieranie tokena nalezacego do usera {}", user.getLogin());
        return tokenRepository.findVerificationTokenByUser(user);
    }


    public Optional<User> getUserByEmailOrLogin(String value) {
        log.info("Pobieranie uzytkownika {}", value);
        return userRepository.findUserDistinctByEmailOrLogin(value, value);
    }

    public void activateUser(User user, VerificationToken verificationToken) {
        log.info("Aktywacja uzytkownika {} i aktualizacja tokenu weryfikacyjnego",user.getLogin());
        user.setEnabled(true);
        userRepository.save(user);
        verificationToken.setIsUsed(true);
        tokenRepository.save(verificationToken);
    }
}
