package com.register.example.dto;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SimpleTweet {
    private String profileImageUrl;
    private String user;
    private String text;
    private LocalDateTime date;
    private String lang;
    private Integer retweetCount;

    public SimpleTweet(String text) {
        this.text = text;
    }

    public static SimpleTweet ofTweet(Tweet tweet) {
        SimpleTweet lightTweet = new SimpleTweet(tweet.getText());
        Date createdAt = tweet.getCreatedAt();
        if (createdAt != null) {
            lightTweet.date = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault());
        }
        TwitterProfile tweetUser = tweet.getUser();
        if (tweetUser != null) {
            lightTweet.user = tweetUser.getName();
            lightTweet.profileImageUrl = tweetUser.getProfileImageUrl();
        }
        lightTweet.lang = tweet.getLanguageCode();
        lightTweet.retweetCount = tweet.getRetweetCount();
        return lightTweet;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLang() {
        return lang;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }
}
