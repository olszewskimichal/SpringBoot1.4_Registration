package com.register.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.register.example.entity.User;
import com.register.example.entity.tokens.PasswordResetToken;
import com.register.example.entity.tokens.VerificationToken;
import com.register.example.forms.EmailRegistrationDTO;
import com.register.example.forms.ResetPasswordForm;
import com.register.example.forms.UserCreateForm;
import com.register.example.jms.EmailProducer;
import com.register.example.repository.PasswordResetTokenRepository;
import com.register.example.repository.UserRepository;
import com.register.example.repository.VerificationTokenRepository;
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

    private final VerificationTokenRepository verificationTokenRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final EmailProducer emailProducer;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, PasswordResetTokenRepository passwordResetTokenRepository, EmailProducer emailProducer) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailProducer = emailProducer;
    }

    @Transactional
    @Modifying
    public User create(UserCreateForm form) throws JsonProcessingException {
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
    public void createVerificationToken(User user) throws JsonProcessingException {
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        log.info("Zaczynam wysylac wiadomosci.");
        emailProducer.send(new EmailRegistrationDTO(user.getEmail(),"Rejestracja",verificationToken.getToken()));
        log.info("Stworzono token dla uzytkownika o id=" + user.getId());
    }

    public void createPasswordResetToken(User user){
        PasswordResetToken token=new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        passwordResetTokenRepository.save(token);
        log.info("Stworzono token dla uzytkownika o id=" + user.getId());
    }

    @Transactional
    @Modifying
    public void delete(User user) {
        log.info("usuwanie uzytkownika o id=" + user.getId());
        Optional<VerificationToken> verificationToken=getVerificationToken(user);
        if (verificationToken.isPresent()) {
            verificationTokenRepository.delete(verificationToken.get());
        }
        userRepository.delete(user.getId());
    }


    public Optional<VerificationToken> getVerificationToken(String token) {
        log.info("Pobieranie tokena {}", token);
        return verificationTokenRepository.findVerificationTokenByToken(token);
    }

    public Optional<VerificationToken> getVerificationToken(User user) {
        log.info("Pobieranie tokena nalezacego do usera {}", user.getLogin());
        return verificationTokenRepository.findVerificationTokenByUser(user);
    }

    public Optional<PasswordResetToken> getPasswordResetToken(String token) {
        log.info("Pobieranie tokena resetujacego haslo {}", token);
        return passwordResetTokenRepository.findPasswordResetTokenByToken(token);
    }

    public Optional<PasswordResetToken> getPasswordResetToken(User user) {
        log.info("Pobieranie tokena resetujacego haslo nalezacego do usera {}", user.getLogin());
        return passwordResetTokenRepository.findPasswordResetTokenByUser(user);
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
        verificationTokenRepository.save(verificationToken);
    }

    @Modifying
    @Transactional
    public User resetPassword(ResetPasswordForm resetPasswordForm) {
        PasswordResetToken token=getPasswordResetToken(resetPasswordForm.getToken()).get();
        User user=token.getUser();
        user.setPasswordHash(new BCryptPasswordEncoder().encode(resetPasswordForm.getPassword()));
        user=userRepository.save(user);
        token.setIsUsed(true);
        passwordResetTokenRepository.save(token);
        return user;
    }
}
