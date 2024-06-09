package com.TweetForge.TweetForge.backend.services;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FeedService {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public FeedService(UserService userService, PostService postService){
        this.userService = userService;
        this.postService = postService;
    }

    public List<Post> getFeedForUser(Integer id){

        ApplicationUser currentUser = userService.getUserById(id);

        Set<ApplicationUser> following =  currentUser.getFollowing();

        Set<Post> currentUserPosts = postService.getAllPostsByAuthor(currentUser);

        List<Post> followingPosts = postService.getAllPostsByAuthors(following);

        List allPosts = new ArrayList();
        allPosts.addAll(currentUserPosts);
        allPosts.addAll(followingPosts);
        return allPosts;
    }
}
