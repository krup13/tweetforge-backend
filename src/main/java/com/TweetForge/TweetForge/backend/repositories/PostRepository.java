package com.TweetForge.TweetForge.backend.repositories;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Set<Post>> findByAuthor(ApplicationUser author);

    @Query("SELECT p FROM Post p WHERE p.author IN (:authors)")
    public List<Post> findPostsByAuthors(@Param("authors") Set<ApplicationUser> authors);
}
