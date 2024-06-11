package com.TweetForge.TweetForge.backend.services;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.repositories.UserRepository;
import com.TweetForge.TweetForge.backend.utils.DiscoveryUserComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DiscoveryService {

    private final UserRepository userRepository;
    private final DiscoveryUserComparator discoveryUserComparator;

    @Autowired
    public DiscoveryService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.discoveryUserComparator = new DiscoveryUserComparator();
    }

    public Set<ApplicationUser> searchForUsers(String searchTerm){
        List<ApplicationUser> usersByUsername = userRepository.findByUsernameContainingIgnoreCase(searchTerm);
        List<ApplicationUser> usersByNickname = userRepository.findByNicknameContainingIgnoreCase(searchTerm);
        List<ApplicationUser> usersByBio = userRepository.findByBioContainingIgnoreCase(searchTerm);
        Set<ApplicationUser> combinedSet = Stream.concat(
                usersByUsername.stream(),
                Stream.concat(usersByNickname.stream(), usersByBio.stream())
        ).collect(Collectors.toSet());

        Set<ApplicationUser> sortedApplicationUserSet = new TreeSet<>(discoveryUserComparator);
        sortedApplicationUserSet.addAll(combinedSet);


        return sortedApplicationUserSet;
    }
}
