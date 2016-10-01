package com.register.example.jBehave.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@Component
public class LoginSteps {


    @Autowired
    private WebApplicationContext context;

    private String password=null;
    private String userLogin=null;
    private MockMvc mvc;
    private RequestBuilder requestBuilder;

    @Given("a")
    public void a_dupa() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        userLogin = "admin";
        password = "admin";
    }

    @When("b")
    public void zaloguje() {
        requestBuilder = post("/login")
                .param("username", userLogin)
                .param("password", password);
    }

    @Then("c")
    public void loginAsAdmin() throws Exception {
        mvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("admin"));
    }


}

