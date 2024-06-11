package com.TweetForge.TweetForge.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class FetchFeedResponseDTO {

    private Integer page;
    private LocalDateTime sessionStart;

    private List<FeedPostDTO> posts;

    public FetchFeedResponseDTO() {
    }

    public FetchFeedResponseDTO(Integer page, LocalDateTime sessionStart, List<FeedPostDTO> posts) {
        this.page = page;
        this.sessionStart = sessionStart;
        this.posts = posts;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(LocalDateTime sessionStart) {
        this.sessionStart = sessionStart;
    }

    public List<FeedPostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<FeedPostDTO> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "FetchFeedReponseDTO{" +
                "page=" + page +
                ", sessionStart=" + sessionStart +
                ", posts=" + posts +
                '}';
    }
}
