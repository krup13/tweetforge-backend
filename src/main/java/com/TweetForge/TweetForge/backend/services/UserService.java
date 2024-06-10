package com.TweetForge.TweetForge.backend.services;

import com.TweetForge.TweetForge.backend.dto.FindUsernameDTO;
import com.TweetForge.TweetForge.backend.exceptions.*;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Image;
import com.TweetForge.TweetForge.backend.models.RegistrationObject;
import com.TweetForge.TweetForge.backend.models.Role;
import com.TweetForge.TweetForge.backend.repositories.RoleRepository;
import com.TweetForge.TweetForge.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Autowired
    public UserService(UserRepository userRepo, RoleRepository roleRepo, MailService mailService, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    public ApplicationUser getUserById(Integer userId) {
        return userRepo.findById(userId).orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            return userRepo.save(user);
        } catch(Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }


    public ApplicationUser registerUser(RegistrationObject ro) {

        ApplicationUser user = new ApplicationUser();

        user.setFirstName(ro.getFirstName());
        user.setLastName(ro.getLastName());
        user.setEmail(ro.getEmail());
        user.setDateOfBirth(ro.getDob());

        String name = user.getFirstName() + user.getLastName();

        boolean nameTaken = true;

        String tempName = "";

        while(nameTaken) {
            tempName = generateUsername(name);

            if(userRepo.findByUsername(tempName).isEmpty()) {
                nameTaken = false;
            }

        }

        user.setUsername(tempName);

        Set<Role> roles = user.getAuthorities();
        roles.add(roleRepo.findByAuthority("USER").get());
        user.setAuthorities(roles);

        try {
            return userRepo.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public void generateEmailVerification(String username) {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        user.setVerification(generateVerificationNumber());

        try {
            mailService.sendEmail(user.getEmail(), "Your verification code", "Here is your verification code: " + user.getVerification());
            userRepo.save(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new EmailFailedToSendException();
        }

        userRepo.save(user);
    }

    public ApplicationUser verifyEmail(String username, Long code) {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        if(code.equals(user.getVerification())) {
            user.setEnabled(true);
            user.setVerification(null);
            return userRepo.save(user);
        } else {
            throw new IncorrectVerificationCodeException();
        }

    }

    public ApplicationUser setPassword(String username, String password) {

        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        String encodedPassword = passwordEncoder.encode(password);

        user.setPassword(encodedPassword);

        return userRepo.save(user);
    }


    private String generateUsername(String name) {
        long generatedNumber = (long) Math.floor(Math.random() * 1_000_000_000);
        return name+generatedNumber;
    }

    private Long generateVerificationNumber() {
        return (long) Math.floor(Math.random() * 100_000_000);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = u.getAuthorities()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        UserDetails ud = new User(u.getUsername(), u.getPassword(), authorities);

        return ud;
    }

    public ApplicationUser setProfileOrBannerPicture(String username, MultipartFile file, String prefix) throws UnableToSavePhotoException {
        ApplicationUser  user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        Image photo = imageService.uploadImage(file, prefix);

        try {
            if(prefix.equals("pfp")) {
                if(user.getProfilePicture() != null && !user.getProfilePicture().getImageName().equals("defaultpfp.png")) {
                    Path p = Paths.get(user.getProfilePicture().getImagePath());
                    Files.deleteIfExists(p);
                }
                user.setProfilePicture(photo);
            } else {
                if(user.getBannerPicture() != null && !user.getBannerPicture().getImageName().equals("defaultbnr.png")) {
                    Path p = Paths.get(user.getBannerPicture().getImagePath());
                    Files.deleteIfExists(p);
                }
                user.setBannerPicture(photo);
            }
        } catch (IOException e) {
            throw new UnableToSavePhotoException();
        }

        return userRepo.save(user);
    }

    public byte[] setUserOrganization(String username, MultipartFile file, String orgName) throws UnableToResolvePhotoException{
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        Image orgImage = imageService.getImageByImageName(orgName)
                .orElseGet(() -> {
                    try{
                        return imageService.createOrganization(file, orgName);
                    } catch(UnableToSavePhotoException e) {
                        return null;
                    }
                });
        if(orgImage != null){

            user.setOrganization(orgImage);
            userRepo.save(user);

            try{
                return Files.readAllBytes(new File(orgImage.getImagePath()).toPath());
            } catch(IOException e){
                throw new UnableToResolvePhotoException();
            }

        } else {
            throw new UnableToResolvePhotoException("We were unable to find or save the organization photo");
        }
    }

    public Set<ApplicationUser> followUser(String user, String followee) throws FollowException{

        if(user.equals(followee)) throw new FollowException();

        ApplicationUser loggedInUser = userRepo.findByUsername(user).orElseThrow(UserDoesNotExistException::new);

        Set<ApplicationUser> followingList = loggedInUser.getFollowing();

        ApplicationUser followedUser = userRepo.findByUsername(followee).orElseThrow(UserDoesNotExistException::new);

        Set<ApplicationUser> followersList = followedUser.getFollowers();

        //Add the followed use to the following list
        followingList.add(followedUser);
        loggedInUser.setFollowing(followingList);

        //Add the current user to the follwer list of the folowee
        followersList.add(loggedInUser);
        followedUser.setFollowers(followersList);

        //update both users
        userRepo.save(loggedInUser);
        userRepo.save(followedUser);

        return loggedInUser.getFollowing();
    }

    public Set<ApplicationUser> retrieveFollowingList(String username) {

        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        return user.getFollowing();
    }

    public Set<ApplicationUser> retrieveFollowersList(String username) {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        return user.getFollowers();
    }

    public String verifyUsername(FindUsernameDTO credential) {
        ApplicationUser user = userRepo.findByEmailOrPhoneOrUsername(credential.getEmail(), credential.getPhone(), credential.getUsername())
                .orElseThrow(UserDoesNotExistException::new);
        return user.getUsername();
    }

    public ApplicationUser getUsersEmailAndPhone(FindUsernameDTO credential) {
        return userRepo.findByEmailOrPhoneOrUsername(credential.getEmail(), credential.getPhone(), credential.getUsername())
                .orElseThrow(UserDoesNotExistException::new);
    }

}
