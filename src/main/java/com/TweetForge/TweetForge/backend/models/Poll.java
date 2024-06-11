package com.TweetForge.TweetForge.backend.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="polls")
public class Poll {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="poll_id")
    private Integer pollId;

    @Column(name="end_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @OneToMany(mappedBy="poll")
    private List<PollChoice> choices;

    public Poll() {
        super();
    }

    public Poll(Integer pollId, LocalDateTime endTime, List<PollChoice> choices) {
        super();
        this.pollId = pollId;
        this.endTime = endTime;
        this.choices = choices;
    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<PollChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<PollChoice> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "Poll [pollId=" + pollId + ", endTime=" + endTime + ", choices=" + choices + "]";
    }
}

