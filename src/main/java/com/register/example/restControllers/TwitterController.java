package com.register.example.restControllers;

import com.register.example.dto.SimpleTweet;
import com.register.example.service.TwitterSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class TwitterController {
    private final TwitterSearch twitterSearch;

    @Autowired
    public TwitterController(TwitterSearch twitterSearch) {
        this.twitterSearch = twitterSearch;
    }

    @RequestMapping(value = "/{searchType}", method = RequestMethod.GET)
    public List<SimpleTweet> search(@PathVariable String searchType, @MatrixVariable List<String> keywords) {
        return twitterSearch.search(searchType, keywords);
    }
}
