package com.register.example.controllers;

import com.register.example.IntegrationTestBase;
import com.register.example.builders.UserCreateFormBuilder;
import com.register.example.entity.User;
import com.register.example.entity.tokens.PasswordResetToken;
import com.register.example.forms.UserCreateForm;
import com.register.example.repository.PasswordResetTokenRepository;
import com.register.example.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResetPasswordControllerTest extends IntegrationTestBase {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void shouldGetPasswordResetPage() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTest5", "loginTest5").withPassword("1").build();
        User user = userService.create(userCreateForm);
        userService.createPasswordResetToken(user);
        Optional<PasswordResetToken> resetToken = userService.getPasswordResetToken(user);

        //when
        RequestBuilder requestBuilder = get("/resetPassword?token=" + resetToken.get().getToken());
        //then
        mvc.perform(requestBuilder)
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(view().name("resetPassword"));
    }

    @Test
    public void shouldFailGettingResetPasswordWithUsedToken() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTestx2", "loginTestx2").withPassword("1")
                .build();
        User user = userService.create(userCreateForm);

        passwordResetTokenRepository.save(new PasswordResetToken("nowyToken", user, Boolean.TRUE));

        //when
        RequestBuilder requestBuilder = get("/resetPassword?token=" + "nowyToken");

        //then
        mvc.perform(requestBuilder);

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(model().attribute("wykorzystany", true))
                .andExpect(content().string(
                        allOf(
                                containsString("<span>Podany token juz został wykorzystany</span>")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void should_fail_passwordReset() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTest6", "loginTest6").withPassword("1").build();
        User user = userService.create(userCreateForm);
        userService.createPasswordResetToken(user);
        Optional<PasswordResetToken> resetToken = userService.getPasswordResetToken(user);

        //when
        mvc.perform(post("/resetPassword")
                .param("pass_field", "zaq1@WSX")
                .param("confirmPassword", "zaq1@WSX1")
                .param("token", resetToken.get().getToken()))
                .andDo(print())
                //then
                .andExpect(content().string(
                        allOf(
                                containsString("<p>Podane hasła nie pasują do siebie</p>")
                        ))
                )
                .andExpect(model().errorCount(1));
    }

    @Test
    public void should_resetPassword_withCorrectData() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTest7", "loginTest7").withPassword("1").build();
        User user = userService.create(userCreateForm);
        userService.createPasswordResetToken(user);
        Optional<PasswordResetToken> resetToken = userService.getPasswordResetToken(user);
        //when
        mvc.perform(post("/resetPassword")
                .param("passForm", "zaq1@WSX")
                .param("confirmPassword", "zaq1@WSX")
                .param("token", resetToken.get().getToken()))
                .andDo(print())
                .andExpect(content().string(
                        allOf(
                                containsString("<span>Twoje hasło zostało zmienione</span>")
                        ))
                )
                //then
                .andExpect(model().errorCount(0));
    }

    @Test
    public void shouldFailGettingResetPasswordWithNotActualToken() throws Exception {

        //when
        RequestBuilder requestBuilder = get("/resetPassword?token=" + "dupa");

        //then
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(model().attribute("blednyPasswordToken", true))
                .andExpect(content().string(
                        allOf(
                                containsString("<span>Błedny link resetujacy hasło</span>")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

}
