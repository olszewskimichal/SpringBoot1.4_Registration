package com.register.example.repository;

import com.register.example.builders.UserBuilder;
import com.register.example.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Before
    public void setUp() throws Exception {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.save(new UserBuilder("user1Email", "user1Login").build());
        userRepository.save(new UserBuilder("user2Email", "user2Login").build());
    }

    @Test
    public void should_return_the_same_person() throws Exception {
        //given
        Optional<User> user = userRepository.findUserDistinctByEmailOrLogin("user1Email", "user1Email");
        //when
        Optional<User> user2 = userRepository.findUserDistinctByEmailOrLogin("user1Login", "user1Login");
        //then
        assertThat(user.get()).isEqualToComparingFieldByField(user2.get());
    }

    @Test
    public void should_be_empty() throws Exception {
        //when
        Optional<User> user = userRepository.findUserDistinctByEmailOrLogin("user3Email", "user3Email");
        //then
        assertThat(user).isEqualTo(Optional.empty());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void should_throw_UsernameNotFoundException() {
        //given
        String expectedUser = "user3Email";
        //when
        Optional<User> user = userRepository.findUserDistinctByEmailOrLogin(expectedUser, expectedUser);
        //then
        user.orElseThrow(() -> new UsernameNotFoundException(String.format("Uzytkownik %s nie istnieje", expectedUser)));
    }


}