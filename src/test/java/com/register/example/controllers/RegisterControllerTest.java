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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class RegisterControllerTest {
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
    public void shoud_show_register_page() throws Exception {
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
                                containsString("<p>Email nie może być  pusty</p>")
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