package com.TweetForge.TweetForge.backend.controllers;

import com.TweetForge.TweetForge.backend.models.Post;
import com.TweetForge.TweetForge.backend.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService){
        this.feedService = feedService;
    }

    @GetMapping("/{userId}")
    public List<Post> getPostsForFeed(@PathVariable("userId") Integer userId){
        List<Post> feedPosts = feedService.getFeedForUser(userId);
        Collections.sort(feedPosts);
        return feedPosts;
    }

}
