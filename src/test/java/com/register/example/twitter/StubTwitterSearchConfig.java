package com.register.example.twitter;

import com.register.example.dto.SimpleTweet;
import com.register.example.service.TwitterSearch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("integrationTest")
public class StubTwitterSearchConfig {
    @Bean
    public TwitterSearch twitterSearch() {
        return (searchType, keywords) -> Arrays.asList(
                new SimpleTweet("Treść tweeta"),
                new SimpleTweet("Treść innego tweeta")
        );
    }
}
