package com.TweetForge.TweetForge.backend.services;

import com.TweetForge.TweetForge.backend.dto.FeedPostDTO;
import com.TweetForge.TweetForge.backend.dto.FetchFeedResponseDTO;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Post;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public FetchFeedResponseDTO getFeedForUser(Integer id, LocalDateTime sessionStart, Integer page){

        ApplicationUser currentUser = userService.getUserById(id);

        Set<ApplicationUser> following = currentUser.getFollowing();
        following.add(currentUser);

        Page<Post> followingPosts = postService.getFeedPage(id, sessionStart, page);
        List<FeedPostDTO> feedPostDTOs = followingPosts.map(post -> {
            FeedPostDTO feedPostDTO = new FeedPostDTO();
            feedPostDTO.setPost(post);
            feedPostDTO.setReply(post.getReply() ? postService.getPostById(post.getReplyTo()) : null);
            feedPostDTO.setRepost(!post.getAuthor().getFollowers().contains(userService.getUserById(id)) && !post.getAuthor().equals(userService.getUserById(id)));
            feedPostDTO.setRepostUser(
                    feedPostDTO.isRepost() ?
                            post.getReposts().stream().filter(user -> userService.getUserById(id).getFollowing().contains(user)).findFirst().orElse(null)
                            :
                            null
            );
            return feedPostDTO;
        }).toList();

        // Map these to a new DTO for the feed itself

        //List<Post> allPosts = new ArrayList<>();
        //allPosts.addAll(currentUserPosts);
        //allPosts.addAll(followingPosts);
        return new FetchFeedResponseDTO(page, sessionStart, feedPostDTOs);
    }
}