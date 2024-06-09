package com.TweetForge.TweetForge.backend.repositories;

import com.TweetForge.TweetForge.backend.models.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Integer> {



}
