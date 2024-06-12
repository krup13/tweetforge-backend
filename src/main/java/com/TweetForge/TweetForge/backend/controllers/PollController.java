package com.TweetForge.TweetForge.backend.controllers;

import com.TweetForge.TweetForge.backend.dto.PollVoteDTO;
import com.TweetForge.TweetForge.backend.models.Poll;
import com.TweetForge.TweetForge.backend.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/poll")
@CrossOrigin("*")
public class PollController {

    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService){
        this.pollService = pollService;
    }

    @PutMapping("/vote")
    public Poll castVote(@RequestBody PollVoteDTO vote){
        return pollService.voteForChoice(vote.getChoiceId(), vote.getUserId());
    }

}
