package com.TweetForge.TweetForge.backend.dto;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Post;
public class FeedPostDTO {
    private Post post;
    private Post replyTo;
    private boolean repost;
    private ApplicationUser repostUser;

    public FeedPostDTO() {
    }

    public FeedPostDTO(Post post, Post reply, boolean repost, ApplicationUser repostUser) {
        this.post = post;
        this.replyTo = reply;
        this.repost = repost;
        this.repostUser = repostUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Post getReplyTo() {
        return replyTo;
    }

    public void setReply(Post replyTo) {
        this.replyTo = replyTo;
    }

    public boolean isRepost() {
        return repost;
    }

    public void setRepost(boolean repost) {
        this.repost = repost;
    }

    public ApplicationUser getRepostUser() {
        return repostUser;
    }

    public void setRepostUser(ApplicationUser repostUser) {
        this.repostUser = repostUser;
    }

    @Override
    public String toString() {
        return "FeedPostDTO{" +
                "post=" + post +
                ", replyTo=" + replyTo +
                ", repost=" + repost +
                ", repostUser=" + repostUser +
                '}';
    }
}
