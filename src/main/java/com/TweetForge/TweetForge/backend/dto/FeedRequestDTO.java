package com.TweetForge.TweetForge.backend.dto;

import java.sql.Date;
import java.time.LocalDateTime;

public class FeedRequestDTO {

    private Integer userId;
    private LocalDateTime sessionStart;
    private Integer page;

    public FeedRequestDTO() {
    }

    public FeedRequestDTO(Integer userId, LocalDateTime sessionStart, Integer page) {
        this.userId = userId;
        this.sessionStart = sessionStart;
        this.page = page;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(LocalDateTime sessionStart) {
        this.sessionStart = sessionStart;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "FeedRequestDTO{" +
                "userId=" + userId +
                ", sessionStart=" + sessionStart +
                ", page=" + page +
                '}';
    }
}
