package com.TweetForge.TweetForge.backend.controllers;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.services.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/discovery")
@CrossOrigin("*")
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @Autowired
    public DiscoveryController(DiscoveryService discoveryService){
        this.discoveryService = discoveryService;
    }

    @GetMapping("/users")
    public Set<ApplicationUser> searchForUsers(@RequestParam String searchTerm){
        return discoveryService.searchForUsers(searchTerm);
    }
}
