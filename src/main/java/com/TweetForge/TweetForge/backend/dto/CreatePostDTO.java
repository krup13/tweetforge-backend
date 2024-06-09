package com.TweetForge.TweetForge.backend.dto;

import com.TweetForge.TweetForge.backend.models.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class CreatePostDTO {

    private String content;
    private ApplicationUser author;
    Set<Post> replies;
    private List<Image> images;
    private Boolean scheduled;
    private Date scheduledDate;
    private Audience audience;
    private ReplyRestriction replyRestriction;
    private Poll poll;

    public CreatePostDTO(){
        super();
    }

    public CreatePostDTO(String content, ApplicationUser author, Set<Post> replies, List<Image> images, Boolean scheduled,
                         Date scheduledDate,Audience audience, ReplyRestriction replyRestriction, Poll poll){
        super();
        this.content = content;
        this.author = author;
        this.replies = replies;
        this.images = images;
        this.scheduled = scheduled;
        this.scheduledDate = scheduledDate;
        this.audience = audience;
        this.replyRestriction = replyRestriction;
        this.poll = poll;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ApplicationUser getAuthor() {
        return author;
    }

    public void setAuthor(ApplicationUser author) {
        this.author = author;
    }

    public Set<Post> getReplies() {
        return replies;
    }

    public void setReplies(Set<Post> replies) {
        this.replies = replies;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Boolean getScheduled() {
        return scheduled;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public ReplyRestriction getReplyRestriction() {
        return replyRestriction;
    }

    public void setReplyRestriction(ReplyRestriction replyRestriction) {
        this.replyRestriction = replyRestriction;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @Override
    public String toString() {
        return "CreatePostDTO{" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", replies=" + replies +
                ", images=" + images +
                ", scheduled=" + scheduled +
                ", scheduledDate=" + scheduledDate +
                ", audience=" + audience +
                ", replyRestriction=" + replyRestriction +
                ", poll=" + poll +
                '}';
    }
}
