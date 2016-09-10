package com.register.example.controllers;

import com.register.example.IntegrationWebTestBase;
import com.register.example.entity.CurrentUser;
import com.register.example.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HomeControllerTest extends IntegrationWebTestBase {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserDetailsService userDetailsService;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldReturnHomePage() throws Exception {
        //given
        CurrentUser currentUser = (CurrentUser) userDetailsService.loadUserByUsername("admin");
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(currentUser, null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
        //when
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(
                        allOf(
                                containsString("<span>Witamy</span>\n" +
                                        "                <strong>" + currentUser.getUser().getName() + "</strong>")
                        ))
                );
    }


}