package com.TweetForge.TweetForge.backend.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Poll;
import com.TweetForge.TweetForge.backend.models.PollChoice;
import com.TweetForge.TweetForge.backend.repositories.PollChoiceRepository;
import com.TweetForge.TweetForge.backend.repositories.PollRepository;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final PollChoiceRepository pollChoiceRepository;
    private final UserService userService;

    @Autowired
    public PollService(PollRepository pollRepository, PollChoiceRepository pollChoiceRepository, UserService userService) {
        this.pollRepository = pollRepository;
        this.pollChoiceRepository = pollChoiceRepository;
        this.userService = userService;
    }

    //Create all the poll options before they are attached to the post
    public PollChoice generateChoice(PollChoice pc) {
        return pollChoiceRepository.save(pc);
    }

    //Create a poll before it gets attached to the post
    public Poll generatePoll(Poll poll) {
        return pollRepository.save(poll);
    }

    //TODO: Update the voteForChoice method to take in the userID and ChoiceID
    //Place a vote on a poll
    public Poll voteForChoice(Integer choiceId, Integer userId) {

        //Grab the user
        ApplicationUser user = userService.getUserById(userId);

        //Get the entire poll from the choice
        PollChoice pc =  pollChoiceRepository.findById(choiceId).orElseThrow();
        Poll poll = pc.getPoll();

        List<ApplicationUser> votes = new ArrayList<ApplicationUser>();
        poll.getChoices().forEach(choice -> {
            choice.getVotes().forEach(voteUser -> {
                votes.add(voteUser);
            });
        });

        if(votes.contains(user)) return poll;

        Set<ApplicationUser> currentVotes = pc.getVotes();
        currentVotes.add(user);
        pc.setVotes(currentVotes);
        pollChoiceRepository.save(pc);

        List<PollChoice> pcList = poll.getChoices();
        pcList.set(poll.getChoices().indexOf(pc), pc);

        return pollRepository.save(poll);
    }

}
