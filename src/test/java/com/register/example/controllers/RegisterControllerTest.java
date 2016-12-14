package com.register.example.controllers;

import com.register.example.IntegrationTestBase;
import com.register.example.builders.UserCreateFormBuilder;
import com.register.example.entity.User;
import com.register.example.entity.tokens.VerificationToken;
import com.register.example.forms.UserCreateForm;
import com.register.example.repository.VerificationTokenRepository;
import com.register.example.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegisterControllerTest extends IntegrationTestBase {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

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
    public void should_show_register_page() throws Exception {
        //when
        mvc.perform(get("/register"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void should_process_registration() throws Exception {
        //when
        mvc.perform(post("/register")
                .param("name", "adam")
                .param("lastName", "malysz")
                .param("login", "malyszNajeprzy")
                .param("email", "a1@o2.pl")
                .param("password", "zaq1@WSX")
                .param("confirmPassword", "zaq1@WSX"))
                .andDo(print())
                //then
                .andExpect(model().errorCount(0));
    }

    @Test
    public void shouldActivateUserWithCorrectToken() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTest1", "loginTest1").withPassword("1").build();
        User user = userService.create(userCreateForm);
        Optional<VerificationToken> verificationToken = userService.getVerificationToken(user);

        //when
        RequestBuilder requestBuilder = get(
                "/register/registrationConfirm?token=" + verificationToken.get().getToken());
        //then
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(model().attribute("aktualny", true))
                .andExpect(content().string(
                        allOf(
                                containsString("<span>Twoje konto zostało aktywowane</span>")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }


    @Test
    public void shouldFailActivationWithUsedToken() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTest2", "loginTest2").withPassword("1").build();
        User user = userService.create(userCreateForm);
        Optional<VerificationToken> verificationToken = userService.getVerificationToken(user);

        //when
        RequestBuilder requestBuilder = get(
                "/register/registrationConfirm?token=" + verificationToken.get().getToken());

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
    public void shouldFailActivationWithNotExistingToken() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTest4", "loginTest4s").withPassword("1")
                .build();
        User user = userService.create(userCreateForm);
        VerificationToken verificationToken = userService.getVerificationToken(user).get();
        verificationToken.setExpiryDate(LocalDateTime.now().minusDays(1).minusMinutes(1));

        verificationTokenRepository.save(verificationToken);

        //when
        RequestBuilder requestBuilder = get("/register/registrationConfirm?token=" + verificationToken.getToken());

        //then
        mvc.perform(requestBuilder);

        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(model().attribute("nieaktualny", true))
                .andExpect(content().string(
                        allOf(
                                containsString("<span>Podany token juz jest nieaktualny</span>")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void shouldFailActivationWithNotActualToken() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTest3", "loginTest3").withPassword("1").build();
        userService.create(userCreateForm);

        //when
        RequestBuilder requestBuilder = get("/register/registrationConfirm?token=" + "dupa");

        //then
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(model().attribute("blednyToken", true))
                .andExpect(content().string(
                        allOf(
                                containsString("<span>Bledny link weryfikacyjny</span>")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }


    @Test
    public void should_fail_registration_with_existing_email() throws Exception {
        //when
        mvc.perform(post("/register")
                .param("name", "adam")
                .param("lastName", "malysz")
                .param("login", "malyszNajeprzy")
                .param("email", "a1@o2.pl")
                .param("password", "zaq1@WSX")
                .param("confirmPassword", "zaq1@WSX"))
                .andDo(print())
                //then
                .andExpect(content().string(
                        allOf(
                                containsString("<p>Podany email lub login jest juz wykorzystany</p>")
                        ))
                )
                .andExpect(model().errorCount(1));
    }

    @Test
    public void should_fail_registration_with_failed_email() throws Exception {
        //when
        mvc.perform(post("/register")
                .param("name", "adam")
                .param("lastName", "malysz")
                .param("login", "malyszNajeprzy3")
                .param("email", "aaaa")
                .param("password", "zaq1@WSX")
                .param("confirmPassword", "zaq1@WSX"))
                .andDo(print())
                //then
                .andExpect(content().string(
                        allOf(
                                containsString("<p>To nie jest prawidłowy adres email</p>")
                        ))
                )
                .andExpect(model().errorCount(1));
    }

    @Test
    public void should_fail_registration() throws Exception {
        //when
        mvc.perform(post("/register"))
                .andDo(print())
                //then
                .andExpect(model().errorCount(5))
                .andExpect(content().string(
                        allOf(
                                containsString("Nazwisko nie może być  puste</p>")
                        ))
                )
                .andExpect(content().string(
                        allOf(
                                containsString("<p>To nie jest prawidłowy adres email</p>")
                        ))
                )
                .andExpect(content().string(
                        allOf(
                                containsString("<p>Login nie może być  pusty</p>")
                        ))
                )
                .andExpect(content().string(
                        allOf(
                                containsString("<p>Imie nie może być  puste</p>")
                        ))
                )
                .andExpect(content().string(
                        allOf(
                                containsString("<p>Podane hasła nie pasują do siebie</p>")
                        ))
                )
                .andExpect(model().hasErrors());
    }


}