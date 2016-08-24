package com.register.example.configuration;

import com.register.example.builders.UserBuilder;
import com.register.example.builders.VerificationTokenBuilder;
import com.register.example.entity.Role;
import com.register.example.entity.User;
import com.register.example.entity.tokens.PasswordResetToken;
import com.register.example.repository.PasswordResetTokenRepository;
import com.register.example.repository.UserRepository;
import com.register.example.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Configuration
@Slf4j
@Profile("!test")
public class DevDBConfig {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    @PostConstruct
    public void populateDatabase() {
        log.info("ładowanie bazy testowej");
        User admin=userRepository.save(new UserBuilder("admin", "admin").withPassword("admin").withRole(Role.ADMIN).withEnabled(true).build());
        User user=userRepository.save(new UserBuilder("user@poczta.pl", "user").withPassword("user").withEnabled(true).build());
        verificationTokenRepository.save(new VerificationTokenBuilder(user,true).build());
        verificationTokenRepository.save(new VerificationTokenBuilder(user,false).withDate(LocalDateTime.now().minusDays(8)).build());
        verificationTokenRepository.save(new VerificationTokenBuilder(user,false).withDate(LocalDateTime.now().minusDays(6)).build());
        resetTokenRepository.save(new PasswordResetToken("testowyToken",user,Boolean.FALSE));
        resetTokenRepository.save(new PasswordResetToken("testowyToken2",user,Boolean.TRUE));
        resetTokenRepository.save(new PasswordResetToken("testowyToken3",user,Boolean.FALSE));
        resetTokenRepository.save(new PasswordResetToken("testowyToken4",user,Boolean.FALSE));
        resetTokenRepository.save(new PasswordResetToken("testowyToken5",user,Boolean.FALSE));

    }
}
