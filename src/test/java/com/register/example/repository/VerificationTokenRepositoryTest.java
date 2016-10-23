package com.register.example.repository;

import com.register.example.builders.UserBuilder;
import com.register.example.entity.User;
import com.register.example.entity.tokens.VerificationToken;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@Ignore
public class VerificationTokenRepositoryTest {
    private static final String TOKEN= UUID.randomUUID().toString();
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private User user;
    private User user2;

    @Before
    public void setUp(){
        user=this.testEntityManager.persist(new UserBuilder("test", "test").build());
        user2=this.testEntityManager.persist(new UserBuilder("user","user").build());
    }

    @Test
    public void shouldFindVerificationTokenByToken(){
        this.testEntityManager.persist(new VerificationToken(TOKEN,user, LocalDateTime.now(),Boolean.FALSE));
        VerificationToken verificationToken=this.verificationTokenRepository.findVerificationTokenByToken(TOKEN).get();
        assertThat(verificationToken.getToken()).isEqualTo(TOKEN);
        assertThat(verificationToken.getUser()).isEqualTo(user);
    }

    @Test
    public void shouldReturnExceptionWhenFindVerificationTokenNotExistingTokenByToken(){
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("Token token nie istnieje");
        this.testEntityManager.persist(new VerificationToken(TOKEN,user, LocalDateTime.now(),Boolean.FALSE));
        VerificationToken verificationToken=this.verificationTokenRepository.findVerificationTokenByToken("token")
                .orElseThrow(() -> new IllegalArgumentException(String.format("Token %s nie istnieje", "token")));
        assertThat(verificationToken).isNull();
    }

    @Test
    public void shouldFindVerificationTokenByUser(){
        this.testEntityManager.persist(new VerificationToken(TOKEN,user, LocalDateTime.now(),Boolean.FALSE));
        VerificationToken verificationToken=this.verificationTokenRepository.findVerificationTokenByUser(user).get();
        assertThat(verificationToken.getToken()).isEqualTo(TOKEN);
        assertThat(verificationToken.getUser()).isEqualTo(user);
    }

    @Test
    public void shouldReturnExceptionWhenFindVerificationTokenNotExistingTokenByUser(){
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("Token uzytkownika user nie istnieje");
        this.testEntityManager.persist(new VerificationToken(TOKEN,user, LocalDateTime.now(),Boolean.FALSE));
        VerificationToken verificationToken=this.verificationTokenRepository.findVerificationTokenByUser(user2)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Token uzytkownika %s nie istnieje", user2.getLogin())));
        assertThat(verificationToken).isNull();
    }

    @Test
    public void shouldDeleteOneTokenBecauseIsUsedAndAnotherOneBecauseIsExpired(){
        this.verificationTokenRepository.deleteAll();
        this.testEntityManager.persist(new VerificationToken(TOKEN,user,LocalDateTime.now(),Boolean.TRUE));
        this.testEntityManager.persist(new VerificationToken(TOKEN,user,LocalDateTime.now().minusDays(8),Boolean.TRUE));
        Integer result=this.verificationTokenRepository.deleteVerificationTokenByExpiryDateLessThen(LocalDateTime.now());
        assertThat(result).isEqualTo(2);
    }


}