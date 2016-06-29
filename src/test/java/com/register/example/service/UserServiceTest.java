package com.register.example.service;

import com.register.example.builders.UserBuilder;
import com.register.example.builders.UserCreateFormBuilder;
import com.register.example.entity.User;
import com.register.example.forms.UserCreateForm;
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

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        userRepository.deleteAll();
        userRepository.save(new UserBuilder("user1Email", "user1Login").build());
        userRepository.save(new UserBuilder("user2Email", "user2Login").build());
        userService = new UserService(userRepository);
    }

    @Test
    public void should_create_user() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email1", "login1").withPassword("1").build();
        //when
        User user = userService.create(userCreateForm);
        //then
        assertThat(user).isNotNull();
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
        userService.delete(allUsers.get(0).getId());
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