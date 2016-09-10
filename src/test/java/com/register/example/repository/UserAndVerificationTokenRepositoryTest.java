package com.register.example.repository;

import com.register.example.IntegrationTestBase;
import com.register.example.builders.UserBuilder;
import com.register.example.builders.VerificationTokenBuilder;
import com.register.example.entity.User;
import com.register.example.entity.tokens.VerificationToken;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class UserAndVerificationTokenRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    private User user;
    private User user1;
    private VerificationToken verificationToken;

    @Before
    public void setUp() throws Exception {
        verificationTokenRepository.deleteAll();
        resetTokenRepository.deleteAll();
        userRepository.deleteAll();
        user=userRepository.save(new UserBuilder("user1Email", "user1Login").build());
        user1=userRepository.save(new UserBuilder("user2Email", "user2Login").build());

        verificationTokenRepository.save(new VerificationTokenBuilder(user,Boolean.FALSE).withDate(LocalDateTime.now().minusDays(8)).build());
        verificationTokenRepository.save(new VerificationTokenBuilder(user,Boolean.TRUE).build());
        verificationToken= verificationTokenRepository.save(new VerificationTokenBuilder(user1,Boolean.FALSE).build());

    }

    @Test
    public void userRepository_should_return_the_same_person() throws Exception {
        //given
        Optional<User> user = userRepository.findUserDistinctByEmailOrLogin("user1Email", "user1Email");
        //when
        Optional<User> user2 = userRepository.findUserDistinctByEmailOrLogin("user1Login", "user1Login");
        //then
        assertThat(user.get()).isEqualToComparingFieldByField(user2.get());
    }

    @Test
    public void userRepository_should_be_empty() throws Exception {
        //when
        Optional<User> user = userRepository.findUserDistinctByEmailOrLogin("user3Email", "user3Email");
        //then
        assertThat(user).isEqualTo(Optional.empty());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void userRepository_should_throw_UsernameNotFoundException() {
        //given
        String expectedUser = "user3Email";
        //when
        Optional<User> user = userRepository.findUserDistinctByEmailOrLogin(expectedUser, expectedUser);
        //then
        user.orElseThrow(() -> new UsernameNotFoundException(String.format("Uzytkownik %s nie istnieje", expectedUser)));
    }


    @Test
    public void testDeleteVerificationTokenByExpiryDateLessThen() throws Exception {
        //given

        //when
        Integer deleted= verificationTokenRepository.deleteVerificationTokenByExpiryDateLessThen(LocalDateTime.now().minusDays(7));

        //then
        assertThat(verificationTokenRepository.findAll().size()).isEqualTo(1);
        assertThat(deleted).isEqualTo(2);
        assertThat(verificationTokenRepository.findAll().get(0)).isEqualToComparingFieldByField(verificationToken);
    }


}