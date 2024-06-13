package com.TweetForge.TweetForge.backend.dto;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Image;

import java.time.LocalDateTime;
import java.util.List;

public class CreateReplyDTO {
    private ApplicationUser author;
    private Integer originalPost;
    private String replyContent;
    private List<Image> images;
    private Boolean scheduled;
    private LocalDateTime scheduledDate;

    public CreateReplyDTO() {
    }

    public CreateReplyDTO(ApplicationUser author, Integer originalPost, String replyContent, List<Image> images, Boolean scheduled, LocalDateTime scheduledDate) {
        this.author = author;
        this.originalPost = originalPost;
        this.replyContent = replyContent;
        this.images = images;
        this.scheduled = scheduled;
        this.scheduledDate = scheduledDate;
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
