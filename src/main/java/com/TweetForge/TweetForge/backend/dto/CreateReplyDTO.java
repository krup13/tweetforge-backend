package com.TweetForge.TweetForge.backend.dto;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Image;
import com.TweetForge.TweetForge.backend.models.Poll;
import com.TweetForge.TweetForge.backend.models.Post;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class CreateReplyDTO {
    private ApplicationUser author;
    private Integer originalPost;
    private String replyContent;
    private List<Image> images;
    private Boolean scheduled;
    private LocalDateTime scheduledDate;
    private Poll poll;

    public CreateReplyDTO() {
    }

    public CreateReplyDTO(ApplicationUser author, Integer originalPost, String replyContent, List<Image> images, Boolean scheduled, LocalDateTime scheduledDate, Poll poll) {
        this.author = author;
        this.originalPost = originalPost;
        this.replyContent = replyContent;
        this.images = images;
        this.scheduled = scheduled;
        this.scheduledDate = scheduledDate;
        this.poll = poll;
    }

    public Integer getOriginalPost() {
        return originalPost;
    }

    public void setOriginalPost(Integer originalPost) {
        this.originalPost = originalPost;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Boolean getScheduled() {
        return scheduled;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public ApplicationUser getAuthor() {
        return author;
    }

    public void setAuthor(ApplicationUser author) {
        this.author = author;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
