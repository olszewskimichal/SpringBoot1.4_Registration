package com.register.example.service;

import com.register.example.builders.UserBuilder;
import com.register.example.builders.UserCreateFormBuilder;
import com.register.example.entity.User;
import com.register.example.entity.VerificationToken;
import com.register.example.forms.UserCreateForm;
import com.register.example.repository.TokenRepository;
import com.register.example.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@Profile("test")
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        tokenRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.save(new UserBuilder("user1Email", "user1Login").build());
        userRepository.save(new UserBuilder("user2Email", "user2Login").build());
        userService = new UserService(userRepository, tokenRepository);
    }

    @Test
    public void should_create_user() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email1", "login1").withPassword("1").build();
        //when
        User user = userService.create(userCreateForm);
        //then
        assertThat(user).isNotNull();
        assertThat(user.getEnabled()).isFalse();
    }

    @Test
    public void should_after_create_user_exist_verificationToken() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email2", "login2").withPassword("1").build();
        //when
        User user = userService.create(userCreateForm);
        //then
        Optional<VerificationToken> verificationToken=tokenRepository.findVerificationTokenByUser(user);
        assertThat(user.getEnabled()).isFalse();
        assertThat(verificationToken.get()).isNotNull();
        assertThat(verificationToken.get().getIsUsed()).isFalse();
        assertThat(verificationToken.get().getUser()).isEqualTo(user);
    }

    @Test
    public void should_after_activateUser_changeToken() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email3", "login3").withPassword("1").build();
        User user = userService.create(userCreateForm);
        Optional<VerificationToken> verificationToken=tokenRepository.findVerificationTokenByUser(user);

        //when
        userService.activateUser(user,verificationToken.get());

        //then
        assertThat(verificationToken.get().getIsUsed()).isTrue();
        assertThat(user.getEnabled()).isTrue();

    }

    @Test(expected = DataIntegrityViolationException.class)
    public void should_throw_exception_withUniqueName() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email1", "login1").withPassword("1").build();
        //when
        userService.create(userCreateForm);
        userService.create(userCreateForm);
        //then
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_exception_withNullPassword() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email1", "login1").build();
        //when
        userService.create(userCreateForm);
        //then
    }

    @Test
    public void should_delete_user() throws Exception {
        //given
        List<User> allUsers = userRepository.findAll();
        Integer size = allUsers.size();
        //when
        userService.delete(allUsers.get(0));
        //then
        assertThat(userRepository.findAll().size()).isEqualTo(size - 1);
    }

    @Test
    public void should_get_user_by_EmailOrLogin() throws Exception {
        //when
        Optional<User> user = userService.getUserByEmailOrLogin("user1Email");
        //then
        assertThat(user.get()).isNotNull();

        //when
        Optional<User> user2 = userService.getUserByEmailOrLogin("user1Login");
        //then
        assertThat(user2.get()).isNotNull();
        assertThat(user.get()).isEqualToComparingFieldByField(user2.get());
    }

    @Test
    public void should_be_empty_by_EmailOrLogin() throws Exception {
        //when
        Optional<User> user = userService.getUserByEmailOrLogin("user3Email");
        //then
        assertThat(user).isEqualTo(Optional.empty());
    }


}