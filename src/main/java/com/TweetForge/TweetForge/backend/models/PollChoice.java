package com.TweetForge.TweetForge.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="poll_choices")
public class PollChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="poll_choice_id")
    private Integer pollChoiceId;

    @ManyToOne
    @JoinColumn(name="poll_id")
    @JsonIgnore
    private Poll poll;

    @Column(name="poll_choice_text")
    private String choiceText;

    @OneToMany
    private Set<ApplicationUser> votes;

    public PollChoice(){
        super();
    }

    public PollChoice(Integer pollChoiceId, Poll poll, String choiceText, Set<ApplicationUser> votes) {
        super();
        this.pollChoiceId = pollChoiceId;
        this.poll = poll;
        this.choiceText = choiceText;
        this.votes = votes;
    }

    public Integer getPollChoiceId() {
        return pollChoiceId;
    }

    public void setPollChoiceId(Integer pollChoiceId) {
        this.pollChoiceId = pollChoiceId;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String pollText) {
        this.choiceText = pollText;
    }

    public Set<ApplicationUser> getVotes() {
        return votes;
    }

    public void setVotes(Set<ApplicationUser> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "PollChoice{" +
                "pollChoiceId=" + pollChoiceId +
                ", poll=" + poll.getPollId() +
                ", choiceText='" + choiceText + '\'' +
                ", votes=" + votes +
                '}';
    }
}
