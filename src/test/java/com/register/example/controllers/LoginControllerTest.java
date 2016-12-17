package com.register.example.controllers;

import com.register.example.IntegrationTestBase;
import com.register.example.builders.UserCreateFormBuilder;
import com.register.example.forms.UserCreateForm;
import com.register.example.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoginControllerTest extends IntegrationTestBase {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private UserService userService;

    private MockMvc mvc;


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void shouldLoginWithCorrectLoginAndPassword() throws Exception {
        //given
        String userLogin = "admin@o2.pl";
        String password = "admin";
        //when
        RequestBuilder requestBuilder = post("/login")
                .param("username", userLogin)
                .param("password", password);
        //then
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("admin@o2.pl"));
    }

    @Test
    public void shouldLoginFailedWithNotActivateAccount() throws Exception {
        //given
        UserCreateForm userCreateForm = new UserCreateFormBuilder("emailTest", "loginTest").withPassword("1").build();
        userService.create(userCreateForm);
        //when
        RequestBuilder requestBuilder = post("/login-error")
                .param("username", "emailTest")
                .param("password", "loginTest");
        //then
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(model().attribute("loginError", true))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }


    @Test
    public void shouldFailLoginAndRedirect() throws Exception {
        //given
        String userLogin = "admin";
        String password = "incorrectPassword";
        //when
        RequestBuilder requestBuilder = formLogin("/login")
                .user("username", userLogin)
                .password("password", password);
        //then
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(redirectedUrl("/login-error"))
                .andExpect(unauthenticated());
    }

    @Test
    public void shouldReturnErrorMessage() throws Exception {
        //when
        mvc.perform(get("/login-error"))
                .andDo(print())
                //then
                .andExpect(model().attribute("loginError", true))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

}


