package com.TweetForge.TweetForge.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="posts")
public class Post implements Comparable<Post> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer post;

    @Column(length = 256, nullable = false)
    private String content;

    @Column(name = "posted_date")
    private Date postedDate;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private ApplicationUser author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_likes_junction",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<ApplicationUser> likes;

    @OneToMany
    private List<Image> images;

    //TODO: figure out video upload

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_reply_junction",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "reply_id")}
    )
    @JsonIgnore
    private Set<Post> replies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_repost_junction",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<ApplicationUser> reposts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_bookmark_junction",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<ApplicationUser> bookmarks;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_view_junction",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<ApplicationUser> views;

    private Boolean schedule;

    @Column(name = "scheduled_date", nullable = true)
    private Date scheduleDate;

    @Enumerated(EnumType.ORDINAL)
    private Audience audience;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "reply_restriction")
    private ReplyRestriction replyRestriction;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="poll_id", referencedColumnName = "poll_id")
    private Poll poll;

    public Post(){
        super();
        this.likes = new HashSet<>();
        this.images = new ArrayList<>();
        this.replies = new HashSet<>();
        this.reposts = new HashSet<>();
        this.bookmarks = new HashSet<>();
        this.views = new HashSet<>();
    }

    public Post(Integer post, String content, Date postedDate, ApplicationUser author,
                Set<ApplicationUser> likes, List<Image> images, Set<Post> replies,
                Set<ApplicationUser> reposts, Set<ApplicationUser> bookmarks, Set<ApplicationUser> views,
                Boolean schedule, Date scheduleDate, Audience audience, ReplyRestriction replyRestriction, Poll poll){
        super();
        this.post = post;
        this.content = content;
        this.postedDate = postedDate;
        this.author = author;
        this.likes = likes;
        this.images = images;
        this.replies = replies;
        this.reposts = reposts;
        this.bookmarks = bookmarks;
        this.views = views;
        this.schedule = schedule;
        this.scheduleDate = scheduleDate;
        this.audience = audience;
        this.replyRestriction = replyRestriction;
        this.poll = poll;
    }

    public Integer getPost() {
        return post;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
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

    public Boolean getSchedule() {
        return schedule;
    }

    public void setSchedule(Boolean schedule) {
        this.schedule = schedule;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
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
        return "Post{" +
                "post=" + post +
                ", content='" + content + '\'' +
                ", postedDate=" + postedDate +
                ", author=" + author +
                ", likes=" + likes +
                ", images=" + images +
                ", replies=" + replies +
                ", reposts=" + reposts +
                ", bookmarks=" + bookmarks +
                ", views=" + views +
                ", schedule=" + schedule +
                ", scheduleDate=" + scheduleDate +
                ", audience=" + audience +
                ", replyRestriction=" + replyRestriction +
                ", poll=" + poll +
                '}';
    }

    @Override
    public int compareTo(Post o) {
        return -this.postedDate.compareTo((o.postedDate));
    }
}
