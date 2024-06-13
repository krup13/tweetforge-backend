package com.TweetForge.TweetForge.backend.repositories;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Integer>{
    
    String FEED_QUERY = "select * from\n" +
            "\t(select post_id, content, posted_date, is_reply, reply_to, author_id from posts where author_id = :id\n" +
            "\tunion\n" +
            "\tselect p.post_id, p.content, p.posted_date, p.is_reply, p.reply_to, p.author_id\n" +
            "\tfrom posts p\n" +
            "\tinner join post_repost_junction prj\n" +
            "\ton p.post_id = prj.post_id where prj.user_id\n" +
            "\tin (select u.user_id as following_id\n" +
            "\t\tfrom users u\n" +
            "\t\tinner join following\n" +
            "\t\ton u.user_id = following.following_id where following.user_id = :id and not following.following_id = :id)\n" +
            "\tunion\n" +
            "\tselect p.post_id, p.content, p.posted_date, p.is_reply, p.reply_to, p.author_id\n" +
            "\tfrom posts p\n" +
            "\twhere p.author_id in (select u.user_id as following_id\n" +
            "\t\t\tfrom users u\n" +
            "\t\t\tinner join following\n" +
            "\t\t\ton u.user_id = following.following_id where following.user_id = :id and not following.following_id = :id)\n" +
            "\t\t\t) as p where p.posted_date <= :session_start order by p.posted_date desc";

    Optional<Set<Post>> findByAuthor(ApplicationUser author);

    @Query(nativeQuery = true, value = FEED_QUERY,
            countQuery = "select count(*) from (" + FEED_QUERY + ")"
    )
    public Page<Post> findFeedPosts(@Param("id") Integer id, @Param("session_start") LocalDateTime sessionDate, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:searchTerm%")
    Set<Post> searchForPosts(@Param("searchTerm") String searchTerm);
}
