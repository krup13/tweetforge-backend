package com.TweetForge.TweetForge.backend.controllers;


import com.TweetForge.TweetForge.backend.exceptions.FollowException;
import com.TweetForge.TweetForge.backend.exceptions.UnableToSavePhotosException;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.services.ImageService;
import com.TweetForge.TweetForge.backend.services.TokenService;
import com.TweetForge.TweetForge.backend.services.UserService;
import com.google.common.net.HttpHeaders;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Set;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {


    private final UserService userService;
    private final TokenService tokenService;
    private final ImageService imageService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService, ImageService imageService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.imageService = imageService;
    }

    @GetMapping("/{username")
    public ApplicationUser getUserByUsername(@PathVariable("username") String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("/verify")
    public ApplicationUser verifyIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION)String token) {
        String username = tokenService.getUsernameFromToken(token);

        return userService.getUserByUsername(username);
    }

    @PostMapping("/pfp")
    public ApplicationUser uploadProfilePicture(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("image") MultipartFile file) throws UnableToSavePhotosException {

        String username = tokenService.getUsernameFromToken(token);
        return userService.setProfileOrBannnerPicture(username, file, "pfp");
    }

    @PostMapping("/banner")
    public ApplicationUser uploadBannerPicture(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("image") MultipartFile file)throws UnableToSavePhotosException{
        String username = tokenService.getUsernameFromToken(token);
        return userService.setProfileOrBannnerPicture(username, file, "banner");
    }

    @PutMapping("/")
    public ApplicationUser updateUser(@RequestBody ApplicationUser u){
        return userService.updateUser(u);
    }

    @ExceptionHandler({FollowException.class})
    public ResponseEntity<String> handleFollowException(){
        return new ResponseEntity<String>("Users cannot follow themselves", HttpStatus.FORBIDDEN);
    }

    @PutMapping("/follow")
    public Set<ApplicationUser> followUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody
    LinkedHashMap<String, String> body) throws FollowException{
        String loggedInUser = tokenService.getUsernameFromToken(token);
        String followedUser = body.get("followedUser");

        return userService.followUser(loggedInUser, followedUser);
    }

    @GetMapping("/following/{username}")
    public Set<ApplicationUser> getFollowingList(@PathVariable("username") String username){
        return userService.retrieveFollowingList(username);
    }

    @GetMapping("/followers/{username}")
    public Set<ApplicationUser> getFollowersList(@PathVariable("username") String username){
        return userService.retrieveFollowersList(username);
    }

}