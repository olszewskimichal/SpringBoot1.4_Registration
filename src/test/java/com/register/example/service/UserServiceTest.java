package com.register.example.service;

import com.register.example.IntegrationTestBase;
import com.register.example.builders.UserBuilder;
import com.register.example.builders.UserCreateFormBuilder;
import com.register.example.entity.User;
import com.register.example.entity.tokens.PasswordResetToken;
import com.register.example.entity.tokens.VerificationToken;
import com.register.example.forms.ResetPasswordForm;
import com.register.example.forms.UserCreateForm;
import com.register.example.jms.EmailProducer;
import com.register.example.repository.PasswordResetTokenRepository;
import com.register.example.repository.UserRepository;
import com.register.example.repository.VerificationTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class UserServiceTest extends IntegrationTestBase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailProducer emailProducer;

    private UserService userService;

    private User user;

    @Before
    public void setUp() throws Exception {
        passwordResetTokenRepository.deleteAll();
        verificationTokenRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(new UserBuilder("user1Email", "user1Login").build());
        userRepository.save(new UserBuilder("user2Email", "user2Login").build());
        userService = new UserService(userRepository, verificationTokenRepository, passwordResetTokenRepository,
                emailProducer);
    }

    @Test
    public void should_create_user() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email1", "login1").withPassword("1").build();
        //when
        user = userService.create(userCreateForm);
        //then
        assertThat(user).isNotNull();
        assertThat(user.getEnabled()).isFalse();
    }

    @Test
    public void should_after_create_user_exist_verificationToken() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email2", "login2").withPassword("1").build();
        //when
        user = userService.create(userCreateForm);
        //then
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findVerificationTokenByUser(user);
        assertThat(user.getEnabled()).isFalse();
        assertThat(verificationToken.get()).isNotNull();
        assertThat(verificationToken.get().getIsUsed()).isFalse();
        assertThat(verificationToken.get().getUser()).isEqualTo(user);
    }

    @Test
    public void should_after_activateUser_changeToken_and_activateUser() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("email3", "login3").withPassword("1").build();
        user = userService.create(userCreateForm);
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findVerificationTokenByUser(user);

        //when
        userService.activateUser(user, verificationToken.get());

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

    @Test
    public void should_createResetPasswordToken() throws Exception {
        //given

        //when
        userService.createPasswordResetToken(user);
        //then
        assertThat(userService.getPasswordResetToken(user).get()).isNotNull();
    }

    @Test
    public void should_afterResetPassword_changePassword() throws Exception {
        //given
        userService.createPasswordResetToken(user);
        Optional<PasswordResetToken> resetToken = userService.getPasswordResetToken(user);
        ResetPasswordForm resetPasswordForm = new ResetPasswordForm(resetToken.get().getToken());
        resetPasswordForm.setPassword("dupa");
        resetPasswordForm.setConfirmPassword("dupa");
        String oldHash = user.getPasswordHash();
        //then
        user = userService.resetPassword(resetPasswordForm);
        String newHash = userService.getUserByEmailOrLogin(user.getLogin()).get().getPasswordHash();

        assertThat(newHash).isNotNull();
        assertThat(newHash).isNotEqualTo(oldHash);
    }


}