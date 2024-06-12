package com.TweetForge.TweetForge.backend.controllers;

import com.TweetForge.TweetForge.backend.dto.FeedPostDTO;
import com.TweetForge.TweetForge.backend.dto.FeedRequestDTO;
import com.TweetForge.TweetForge.backend.dto.FetchFeedResponseDTO;
import com.TweetForge.TweetForge.backend.models.Post;
import com.TweetForge.TweetForge.backend.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/feed")
@CrossOrigin("*")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService){
        this.feedService = feedService;
    }

    @PostMapping
    public FetchFeedResponseDTO getPostsForFeed(@RequestBody FeedRequestDTO feedRequest){
        System.out.println(feedRequest);
        return feedService.getFeedForUser(feedRequest.getUserId(), feedRequest.getSessionStart(), feedRequest.getPage());
    }

}
