package com.TweetForge.TweetForge.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;

@Entity
@Table(name="posts")
public class Post implements Comparable<Post>{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="post_id")
    private Integer postId;

    @Column(length=512, nullable=false)
    private String content;

    @Column(name="posted_date")
    private LocalDateTime postedDate;

    @Column(name="is_reply", nullable=false)
    private Boolean reply=true;

    @Column(name="reply_to")
    private Integer replyTo;

    @ManyToOne
    @JoinColumn(name="author_id", nullable=false)
    private ApplicationUser author;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="post_likes_junction",
            joinColumns= {@JoinColumn(name="post_id")},
            inverseJoinColumns= {@JoinColumn(name="user_id")}
    )
    private Set<ApplicationUser> likes;

    @OneToMany
    private List<Image> images;

    //TODO: Figure out video upload

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="post_reply_junction",
            joinColumns= {@JoinColumn(name="post_id")},
            inverseJoinColumns = {@JoinColumn(name="reply_id")}
    )
    private Set<Post> replies;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="post_repost_junction",
            joinColumns = {@JoinColumn(name="post_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")}
    )
    private Set<ApplicationUser> reposts;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="post_bookmark_junction",
            joinColumns= {@JoinColumn(name="post_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")}
    )
    private Set<ApplicationUser> bookmarks;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="post_view_junction",
            joinColumns= {@JoinColumn(name="post_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")}
    )
    private Set<ApplicationUser> views;

    @Column(name="scheduled", nullable=false)
    private Boolean scheduled=false;

    @Column(name="scheduled_date", nullable=true)
    private LocalDateTime scheduledDate;

    @Enumerated(EnumType.ORDINAL)
    private Audience audience;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="reply_restriction")
    private ReplyRestriction replyRestriction;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="poll_id", referencedColumnName="poll_id")
    private Poll poll;

    public Post() {
        super();
        this.likes = new HashSet<>();
        this.images = new ArrayList<>();
        this.replies = new HashSet<>();
        this.reposts = new HashSet<>();
        this.bookmarks = new HashSet<>();
        this.views = new HashSet<>();
    }

    public Post(Integer postId, String content, LocalDateTime postedDate, Boolean reply, Integer replyTo, ApplicationUser author, Set<ApplicationUser> likes, List<Image> images, Set<Post> replies, Set<ApplicationUser> reposts, Set<ApplicationUser> bookmarks, Set<ApplicationUser> views, Boolean scheduled, LocalDateTime scheduledDate, Audience audience, ReplyRestriction replyRestriction, Poll poll) {
        this.postId = postId;
        this.content = content;
        this.postedDate = postedDate;
        this.reply = reply;
        this.replyTo = replyTo;
        this.author = author;
        this.likes = likes;
        this.images = images;
        this.replies = replies;
        this.reposts = reposts;
        this.bookmarks = bookmarks;
        this.views = views;
        this.scheduled = scheduled;
        this.scheduledDate = scheduledDate;
        this.audience = audience;
        this.replyRestriction = replyRestriction;
        this.poll = poll;
    }

    public Integer getPost() {
        return postId;
    }

    public void setPost(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public ApplicationUser getAuthor() {
        return author;
    }

    public void setAuthor(ApplicationUser author) {
        this.author = author;
    }

    public Set<ApplicationUser> getLikes() {
        return likes;
    }

    public void setLikes(Set<ApplicationUser> likes) {
        this.likes = likes;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Set<Post> getReplies() {
        return replies;
    }

    public void setReplies(Set<Post> replies) {
        this.replies = replies;
    }

    public Set<ApplicationUser> getReposts() {
        return reposts;
    }

    public void setReposts(Set<ApplicationUser> reposts) {
        this.reposts = reposts;
    }

    public Set<ApplicationUser> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Set<ApplicationUser> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Set<ApplicationUser> getViews() {
        return views;
    }

    public void setViews(Set<ApplicationUser> views) {
        this.views = views;
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

    public Boolean getReply() {
        if(reply == null){
            return false;
        }
        return reply;
    }

    public void setReply(Boolean reply) {
        this.reply = reply;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Integer replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", content='" + content + '\'' +
                ", postedDate=" + postedDate +
                ", reply=" + reply +
                ", replyTo=" + replyTo +
                ", author=" + author +
                ", likes=" + likes +
                ", images=" + images +
                ", replies=" + replies +
                ", reposts=" + reposts +
                ", bookmarks=" + bookmarks +
                ", views=" + views +
                ", scheduled=" + scheduled +
                ", scheduledDate=" + scheduledDate +
                ", audience=" + audience +
                ", replyRestriction=" + replyRestriction +
                ", poll=" + poll +
                '}';
    }

    @Override
    public int compareTo(Post o) {
        return -this.postedDate.compareTo(o.postedDate);
    }
}
