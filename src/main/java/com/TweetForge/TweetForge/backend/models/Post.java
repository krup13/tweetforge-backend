package com.TweetForge.TweetForge.backend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // Denote whether the post HAS A REPLY
    @Column(name="is_reply", nullable=false)
    private Boolean reply=false;

    // If reply is true, this field denote the post_id of the replying post
    @Column(name="reply_to")
    private Integer replyTo;

    @ManyToOne
    @JoinColumn(name="author_id", nullable=false)
    private ApplicationUser author;
    
    /**
     * From the post_likes_junction table, obtain all rows where post_id equals
     * this.post_id and from those rows, fetch all users defined in user_id columns
     * using FetchType.LAZY strategy.
     */
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="post_likes_junction",
            joinColumns= {@JoinColumn(name="post_id")},
            inverseJoinColumns= {@JoinColumn(name="user_id")}
    )
    private Set<ApplicationUser> likes;

    @OneToMany
    private List<Image> images;

    //TODO: Figure out video upload

    
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="post_reply_junction",
            joinColumns= {@JoinColumn(name="post_id")},
            inverseJoinColumns = {@JoinColumn(name="reply_id")}
    )
    private Set<Post> replies;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="post_repost_junction",
            joinColumns = {@JoinColumn(name="post_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")}
    )
    private Set<ApplicationUser> reposts;

    @Enumerated(EnumType.ORDINAL)
    private Audience audience = Audience.EVERYONE;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="reply_restriction")
    private ReplyRestriction replyRestriction = ReplyRestriction.EVERYONE;

    public Post() {
        super();
        this.likes = new HashSet<>();
        this.images = new ArrayList<>();
        this.replies = new HashSet<>();
        this.reposts = new HashSet<>();
    }

    public Post(Integer postId, String content, LocalDateTime postedDate, Boolean reply, Integer replyTo, ApplicationUser author, Set<ApplicationUser> likes, List<Image> images, Set<Post> replies, Set<ApplicationUser> reposts, Set<ApplicationUser> bookmarks, Set<ApplicationUser> views, Boolean scheduled, LocalDateTime scheduledDate, Audience audience, ReplyRestriction replyRestriction) {
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
        this.audience = audience;
        this.replyRestriction = replyRestriction;
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
                ", audience=" + audience +
                ", replyRestriction=" + replyRestriction +
                '}';
    }

    @Override
    public int compareTo(Post o) {
        return -this.postedDate.compareTo(o.postedDate);
    }
}
