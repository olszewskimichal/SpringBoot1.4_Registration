package com.register.example.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).addFilter(springSecurityFilterChain).apply(springSecurity()).build();
    }

    @Test
    public void shouldLoginWithCorrectLoginAndPassword() throws Exception {
        //given
        String userLogin="admin";
        String password="admin";
        //when
        RequestBuilder requestBuilder = post("/login")
                .param("username", userLogin)
                .param("password", password);
        //then
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("admin"));
    }


    @Test
    public void shouldFailLoginAndRedirect() throws Exception {
        //given
        String userLogin="admin";
        String password="incorrectPassword";
        //when
        RequestBuilder requestBuilder = formLogin("/login")
                .user("username",userLogin)
                .password("password",password);
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
                .andExpect(model().attribute("errorMessage","Nieprawidłwy użytkownik lub hasło"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
    
}

