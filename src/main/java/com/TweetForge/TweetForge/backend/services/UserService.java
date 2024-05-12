package com.TweetForge.TweetForge.backend.services;

import com.TweetForge.TweetForge.backend.exceptions.EmailAlreadyTakenException;
import com.TweetForge.TweetForge.backend.exceptions.UserDoesNotExistException;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.RegistrationObject;
import com.TweetForge.TweetForge.backend.models.Role;
import com.TweetForge.TweetForge.backend.repositories.RoleRepository;
import com.TweetForge.TweetForge.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SqlInitializationAutoConfiguration sqlInitializationAutoConfiguration;

    @Autowired //auto-inject dependencies into this constructor
    public UserService(UserRepository userRepository, RoleRepository roleRepository, SqlInitializationAutoConfiguration sqlInitializationAutoConfiguration) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sqlInitializationAutoConfiguration = sqlInitializationAutoConfiguration;
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

        userRepository.save(user);
    }

    private String generateUsername(String name) {
        long generatedNumber = (long) Math.floor(Math.random() * 1_000_000_000);
        return name+generatedNumber;
    }

    private long generateVerificationNumber() {
       return (long) Math.floor(Math.random() * 1_000_000_000);
    }

}