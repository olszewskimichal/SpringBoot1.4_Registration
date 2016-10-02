package com.register.example.service;

import com.register.example.dto.SimpleTweet;

import java.util.List;

public interface TwitterSearch {
    List<SimpleTweet> search(String searchType, List<String> keywords);
}
