package com.register.example.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
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
    public void should_fail_registration() throws Exception {
        //when
        mvc.perform(post("/register"))
                .andDo(print())
        //then
                .andExpect(model().hasErrors());
    }


}