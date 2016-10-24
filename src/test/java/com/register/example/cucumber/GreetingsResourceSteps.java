package com.register.example.cucumber;

import com.register.example.IntegrationTestBase;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class GreetingsResourceSteps extends IntegrationTestBase {

    @Autowired
    private org.springframework.boot.test.web.client.TestRestTemplate restTemplate;

    private String caller; // input

    private ResponseEntity<String> response; // output

    @Given("I use the caller (.*)")
    public void useCaller(String caller) {
        this.caller = caller;
    }

    @When("I request a greeting")
    public void requestGreeting() {
        response = restTemplate
                .exchange("/greetings/{caller}", HttpMethod.GET, null, String.class, caller);
    }

    @Then("I should get a response with HTTP status code (.*)")
    public void shouldGetResponseWithHttpStatusCode(int statusCode) {
        assertThat(response.getStatusCodeValue()).isEqualTo(statusCode);
    }

    @And("The response should contain the message (.*)")
    public void theResponseShouldContainTheMessage(String message) {
        assertThat(response.getBody()).isEqualTo(message);
    }

    @Test
    public void test(){

    }

}