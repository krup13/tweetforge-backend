package com.TweetForge.TweetForge.backend.services;

import com.TweetForge.TweetForge.backend.dto.FindUsernameDTO;
import com.TweetForge.TweetForge.backend.exceptions.*;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Image;
import com.TweetForge.TweetForge.backend.models.RegistrationObject;
import com.TweetForge.TweetForge.backend.models.Role;
import com.TweetForge.TweetForge.backend.repositories.RoleRepository;
import com.TweetForge.TweetForge.backend.repositories.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Autowired //auto-inject dependencies into this constructor
    public UserService(UserRepository userRepository, RoleRepository roleRepository, MailService mailService, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    public ApplicationUser getUserById(Integer userId){
        return userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try{
            return userRepository.save(user);
        } catch (Exception e){
            throw new EmailAlreadyTakenException();
        }
    }

    //if any problems encountered, refer ep. 3
    public ApplicationUser registerUser(RegistrationObject registrationObject) {
        ApplicationUser user = new ApplicationUser();

        user.setFirstName(registrationObject.getFirstName());
        user.setLastName(registrationObject.getLastName());
        user.setEmail(registrationObject.getEmail());
        user.setDateOfBirth(registrationObject.getDob());

        String name = user.getFirstName() + user.getLastName();

        boolean nameTaken = true;

        String tempName = "";
        while (nameTaken) {
            tempName = generateUsername(name);

            if (userRepository.findByUsername(tempName).isEmpty()){
                nameTaken=false;
            }
        }

        user.setUsername(tempName);

        Set<Role> roles = user.getAuthorities();

        if (roles == null) {
            roles = new HashSet<>();
            roles.add(roleRepository.findByAuthority("USER").get());
            user.setAuthorities(roles);
        } else {
            roles.add(roleRepository.findByAuthority("USER").get());
            user.setAuthorities(roles);
        }

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public void generateEmailVerification(String username) {
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        user.setVerificationCode(generateVerificationNumber());

        try {
            mailService.sendEmail(user.getEmail(), "Your verification code", "Here is your verification code : " + user.getVerificationCode());
            userRepository.save(user);
        } catch (Exception e) {
            throw new EmailFailedToSendException();
        }
        userRepository.save(user);
    }

    public ApplicationUser verifyEmail (String username, Long code) {
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        if(code.equals(user.getVerificationCode())){
            user.setEnabled(true);
            user.setVerificationCode(null); //recheck in ep12
            return userRepository.save(user);
        } else {
            throw new IncorrectVerificationCodeException();
        }
    }

    public ApplicationUser setPassword(String username, String password){
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        String encodedPassword = passwordEncoder.encode(password);

        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    private String generateUsername(String name) {
        long generatedNumber = (long) Math.floor(Math.random() * 1_000_000_000);
        return name+generatedNumber;
    }

    private Long generateVerificationNumber() {
       return (long) Math.floor(Math.random() * 1_000_000_000);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser u = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = u.getAuthorities()
                        .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        UserDetails ud = new User(u.getUsername(), u.getPassword(), authorities);

        return ud;
    }

    public ApplicationUser setProfileOrBannnerPicture(String username, MultipartFile file, String prefix)throws UnableToSavePhotosException{
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        Image photo = imageService.uploadImage(file, prefix);

        try{
            if(prefix.equals("pfp")){
                if(user.getProfilePicture() != null && user.getProfilePicture().getImageName().equals("defaultpfp.png")){
                    Path p = Paths.get(user.getProfilePicture().getImagePath());
                    Files.deleteIfExists(p);
                }
                user.setProfilePicture(photo);
            }else {
                if(user.getBannerPicture() != null && user.getBannerPicture().getImageName().equals("defaultbnr.png"));
                Path p = Paths.get(user.getBannerPicture().getImagePath());
                Files.deleteIfExists(p);
            }
            user.setBannerPicture(photo);
        }
        catch (IOException e){
            throw new UnableToSavePhotosException();
        }

        return userRepository.save(user);
    }

    public byte[] setUserOrganization(String username, MultipartFile file, String orgName) throws UnableToResolvePhotoException {
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        Image orgImage = imageService.getImageByImageName(orgName)
                .orElseGet(() -> {
                    try{
                        return imageService.createOrganization(file, orgName);
                    }catch(UnableToSavePhotosException e){
                        return null;
                    }
                });
        if(orgImage != null){
            //ep 149 minit 15:20
        }else{
            throw new UnableToResolvePhotoException("We were unable to find or save the organization photo");
        }

    }

    public Set<ApplicationUser> followUser(String user, String followee) throws FollowException{

        if(user.equals(followee))throw new FollowException();

        ApplicationUser loggedInUser = userRepository.findByUsername(user).orElseThrow(UserDoesNotExistException::new);

        Set<ApplicationUser> followingList = loggedInUser.getFollowing();

        ApplicationUser followedUser = userRepository.findByUsername(followee).orElseThrow(UserDoesNotExistException::new);

        Set<ApplicationUser> followersList = followedUser.getFollowers();

        //Add the followed user to the following list
        followingList.add(followedUser);
        loggedInUser.setFollowing(followingList);

        //Add the current user to the follower list of the followee
        followersList.add(loggedInUser);
        followedUser.setFollowers(followersList);

        //update both users
        userRepository.save(loggedInUser);
        userRepository.save(followedUser);

        return loggedInUser.getFollowing();
    }

    public Set<ApplicationUser> retrieveFollowingList(String username) {

        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        return user.getFollowing();
    }

    public Set<ApplicationUser> retrieveFollowersList(String username) {
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        return user.getFollowers();
    }

    public String verifyUsername(FindUsernameDTO credential){
        ApplicationUser user = userRepository.findByEmailOrPhoneOrUsername(credential.getEmail(), credential.getPhone(), credential.getUsername())
                .orElseThrow(UserDoesNotExistException::new);
        return user.getUsername();
    }

    public ApplicationUser getUsersEmailAndPhone(FindUsernameDTO credential){
        return userRepository.findByEmailOrPhoneOrUsername(credential.getEmail(), credential.getPhone(), credential.getUsername())
                .orElseThrow(UserDoesNotExistException::new);
    }
}
