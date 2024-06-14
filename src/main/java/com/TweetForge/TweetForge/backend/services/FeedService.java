package com.TweetForge.TweetForge.backend.services;

import com.TweetForge.TweetForge.backend.dto.FeedPostDTO;
import com.TweetForge.TweetForge.backend.dto.FetchFeedResponseDTO;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FeedService {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public FeedService(UserService userService, PostService postService){
        this.userService = userService;
        this.postService = postService;
    }

    public FetchFeedResponseDTO getFeedForUser(Integer id, LocalDateTime sessionStart, Integer page, Optional<String> searchTerm){
        List<Post> allPosts = new ArrayList<>();

        ApplicationUser currentUser = userService.getUserById(id);

        Set<ApplicationUser> following = currentUser.getFollowing();
        following.add(currentUser);
        
        if (searchTerm.isEmpty()) {
            allPosts.addAll(postService.getAllPosts());
        }
        else {
            allPosts.addAll(postService.getAllFeedPosts(id));
        }
        List<FeedPostDTO> feedPostDTOs = new ArrayList<>();
        
        for(int i = 0; i < allPosts.size(); i++) {
            FeedPostDTO feedPostDTO = new FeedPostDTO();
            Post post = allPosts.get(i);
            feedPostDTO.setPost(post);
            feedPostDTO.setReply(post.getReply() ? postService.getPostById(post.getReplyTo()) : null);
            feedPostDTO.setRepost(!post.getAuthor().getFollowers().contains(userService.getUserById(id)) && !post.getAuthor().equals(userService.getUserById(id)));
            feedPostDTO.setRepostUser(
                    feedPostDTO.isRepost() ?
                            post.getReposts().stream().filter(user -> userService.getUserById(id).getFollowing().contains(user)).findFirst().orElse(null)
                            :
                            null
            );
            feedPostDTOs.add(feedPostDTO);
        }
        // Map these to a new DTO for the feed itself

        //List<Post> allPosts = new ArrayList<>();
        //allPosts.addAll(currentUserPosts);
        //allPosts.addAll(followingPosts);
        return new FetchFeedResponseDTO(page, sessionStart, feedPostDTOs);
    }
}