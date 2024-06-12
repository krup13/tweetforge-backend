package com.TweetForge.TweetForge.backend.controllers;

import java.util.List;
import java.util.Set;

import com.TweetForge.TweetForge.backend.dto.CreatePostDTO;
import com.TweetForge.TweetForge.backend.dto.CreateReplyDTO;
import com.TweetForge.TweetForge.backend.exceptions.PostDoesNotExistException;
import com.TweetForge.TweetForge.backend.exceptions.UnableToCreatePostException;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Post;
import com.TweetForge.TweetForge.backend.services.PostService;
import com.TweetForge.TweetForge.backend.services.UserService;
import com.google.common.net.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@CrossOrigin("*")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @ExceptionHandler({UnableToCreatePostException.class})
    public ResponseEntity<String> handleUnableToCreatePost(){
        return new ResponseEntity<String>("Unable to create post at this time", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/")
    public Post createPost(@RequestBody CreatePostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PostMapping("/reply")
    public Post createReply(@RequestBody CreateReplyDTO replyDTO){
        return postService.createReply(replyDTO);
    }

    @PostMapping(value="/reply/media", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Post createMediaReply(@RequestPart("reply") String reply, @RequestPart("media") List<MultipartFile> files){
        return postService.createReplyWithMedia(reply, files);
    }

    @PostMapping(value="/media", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Post createMediaPost(@RequestPart("post") String post, @RequestPart("media") List<MultipartFile> files) {
        return postService.createMediaPost(post, files);
    }

    @ExceptionHandler({PostDoesNotExistException.class})
    public ResponseEntity<String> handlePostDoesNotExist(){
        return new ResponseEntity<String>("Post does not exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public Post getPostById(@PathVariable("id") int id) {
        return postService.getPostById(id);
    }

    @GetMapping("/author/{userId}")
    public Set<Post> getPostsByAuthor(@PathVariable("userId") Integer userId){
        ApplicationUser author = userService.getUserById(userId);
        return postService.getAllPostsByAuthor(author);
    }

    @PutMapping("/repost/{id}")
    public Post repost(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return postService.repost(id, token);
    }

    @PutMapping("/like/{id}")
    public Post like(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return postService.like(id, token);
    }

    @PutMapping("/bookmark/{id}")
    public Post bookmark(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return postService.bookmark(id, token);
    }

    @PutMapping("/view/{id}")
    public Post view(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return postService.viewPost(id, token);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deletePost(@RequestBody Post p){
        postService.deletePost(p);
        return new ResponseEntity<String>("Post has been deleted", HttpStatus.OK);
    }

}
