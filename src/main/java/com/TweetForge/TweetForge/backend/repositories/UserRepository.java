package com.TweetForge.TweetForge.backend.repositories;
//sends the information to the database

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //if not, not going to be created as a bean
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    Optional<ApplicationUser> findByUsername(String username);
    Optional<ApplicationUser>findByEmailOrPhoneOrUsername(String email, String phone, String username);
    List<ApplicationUser> findByUsernameContainingIgnoreCase(String username);
    List<ApplicationUser> findByNicknameContainingIgnoreCase(String nickname);
    List<ApplicationUser> findByBioContainingIgnoreCase(String bio);
}
