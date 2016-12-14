package com.register.example.service;

import com.register.example.builders.UserBuilder;
import com.register.example.entity.User;
import com.register.example.jms.EmailProducer;
import com.register.example.repository.PasswordResetTokenRepository;
import com.register.example.repository.UserRepository;
import com.register.example.repository.VerificationTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserServiceMockTest {

    @Mock
    UserRepository userRepository;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private EmailProducer emailProducer;

    @Autowired
    @InjectMocks
    private UserService userService;

    private User user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, verificationTokenRepository, passwordResetTokenRepository,
                emailProducer);
    }

    @Test
    public void should_User_repository_create_user() throws Exception {
        //given
        User user = new UserBuilder("email1", "login1").build();
        long userId = 12345;
        User persistedUser = new UserBuilder("email1", "login1").build();
        user.setEmail("email1");
        persistedUser.setEmail("email1");
        persistedUser.setId(userId);
        //when
        when(userRepository.save(user)).thenReturn(persistedUser);
        User result = userRepository.save(user);
        verify(userRepository).save(user);

        assertThat(result).isEqualToComparingFieldByField(persistedUser);
    }

}