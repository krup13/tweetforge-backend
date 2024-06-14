package com.TweetForge.TweetForge.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.TweetForge.TweetForge.backend.dto.CreatePostDTO;
import com.TweetForge.TweetForge.backend.dto.CreateReplyDTO;
import com.TweetForge.TweetForge.backend.exceptions.PostDoesNotExistException;
import com.TweetForge.TweetForge.backend.exceptions.UnableToCreatePostException;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.Audience;
import com.TweetForge.TweetForge.backend.models.Image;
import com.TweetForge.TweetForge.backend.models.Post;
import com.TweetForge.TweetForge.backend.models.ReplyRestriction;
import com.TweetForge.TweetForge.backend.repositories.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepo;
    private final ImageService imageService;
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepo, ImageService imageService,
                       TokenService tokenService, UserService userService) {
        this.postRepo = postRepo;
        this.imageService = imageService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    public Post createPost(CreatePostDTO dto) {
        System.out.println(dto);

        Image savedGif;

        // If true, there is a single gif from tenor
        if (dto.getImages() != null && dto.getImages().size() > 0) {
            System.out.println(dto.getImages());
            List<Image> gifList = dto.getImages();
            Image gif = gifList.get(0);
            gif.setImagePath(gif.getImageURL());

            savedGif = imageService.saveGifFromPost(gif);
            gifList.remove(0);
            gifList.add(savedGif);
            dto.setImages(gifList);
        }

        Post p = new Post();
        p.setContent(dto.getContent());
        p.setPostedDate(LocalDateTime.now());
        System.out.println(LocalDateTime.now());
        p.setAuthor(dto.getAuthor());
        p.setReplies(dto.getReplies());
        p.setAudience(dto.getAudience());
        p.setReplyRestriction(dto.getReplyRestriction());
        p.setImages(dto.getImages());

        try {
            Post posted = postRepo.save(p);
            return posted;
        } catch (Exception e) {
            // TODO: Setup custom exception
            throw new UnableToCreatePostException();
        }

    }

    public Post createMediaPost(String post, List<MultipartFile> files) {
        CreatePostDTO dto = new CreatePostDTO();

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            dto = objectMapper.readValue(post, CreatePostDTO.class);

            Post p = new Post();
            p.setContent(dto.getContent());
            if (dto.getScheduled()) {
                p.setPostedDate(dto.getScheduledDate());
            } else {
                p.setPostedDate(LocalDateTime.now());
            }
            p.setAuthor(dto.getAuthor());
            p.setReplies(dto.getReplies());
            p.setAudience(dto.getAudience());
            p.setReplyRestriction(dto.getReplyRestriction());

            // Upload the images that got passed
            List<Image> postImages = new ArrayList<>();

            for (int i = 0; i < files.size(); i++) {
                Image postImage = imageService.uploadImage(files.get(i), "post");
                postImages.add(postImage);
            }

            p.setImages(postImages);

            return postRepo.save(p);

        } catch (Exception e) {
            throw new UnableToCreatePostException();
        }

    }

    public Post createReply(CreateReplyDTO dto) {

        System.out.println("Create reply dto: " + dto);

        CreatePostDTO postDTO = new CreatePostDTO(
                dto.getReplyContent(),
                dto.getAuthor(),
                new HashSet<>(),
                dto.getImages(),
                dto.getScheduled(),
                dto.getScheduledDate(),
                Audience.EVERYONE,
                ReplyRestriction.EVERYONE);

        Post reply = createPost(postDTO);
        reply.setReply(true);
        reply.setReplyTo(dto.getOriginalPost());

        Post original = postRepo.findById(dto.getOriginalPost()).orElseThrow(UnableToCreatePostException::new);
        Set<Post> originalPostReplies = original.getReplies();
        originalPostReplies.add(reply);
        original.setReplies(originalPostReplies);

        postRepo.save(original);

        return postRepo.save(reply);
    }

    public Post createReplyWithMedia(String reply, List<MultipartFile> files) {
        CreateReplyDTO dto = new CreateReplyDTO();
        ObjectMapper mapper = new ObjectMapper();
        try {
            dto = mapper.readValue(reply, CreateReplyDTO.class);

            CreatePostDTO postDTO = new CreatePostDTO(
                    dto.getReplyContent(),
                    dto.getAuthor(),
                    new HashSet<>(),
                    dto.getImages(),
                    dto.getScheduled(),
                    dto.getScheduledDate(),
                    Audience.EVERYONE,
                    ReplyRestriction.EVERYONE);

            Post replyPost = createPost(postDTO);
            replyPost.setReply(true);
            replyPost.setReplyTo(dto.getOriginalPost());

            Post original = postRepo.findById(dto.getOriginalPost()).orElseThrow(UnableToCreatePostException::new);
            Set<Post> originalPostReplies = original.getReplies();
            originalPostReplies.add(replyPost);
            original.setReplies(originalPostReplies);

            postRepo.save(original);

            // Upload the images that got passed
            List<Image> postImages = new ArrayList<>();

            for (int i = 0; i < files.size(); i++) {
                Image postImage = imageService.uploadImage(files.get(i), "post");
                postImages.add(postImage);
            }

            replyPost.setImages(postImages);

            return postRepo.save(replyPost);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToCreatePostException();
        }
    }

    public Post repost(Integer postId, String token) {
        String username = tokenService.getUsernameFromToken(token);
        ApplicationUser user = userService.getUserByUsername(username);

        Post post = postRepo.findById(postId).orElseThrow(PostDoesNotExistException::new);

        Set<ApplicationUser> reposts = post.getReposts();
        if (reposts.contains(user)) {
            reposts = reposts.stream().filter(u -> u.getUserId() != user.getUserId()).collect(Collectors.toSet());
        } else {
            reposts.add(user);

        }
        post.setReposts(reposts);

        return postRepo.save(post);
    }

    public Post like(Integer postId, String token) {
        String username = tokenService.getUsernameFromToken(token);
        ApplicationUser user = userService.getUserByUsername(username);

        Post post = postRepo.findById(postId).orElseThrow(PostDoesNotExistException::new);

        Set<ApplicationUser> likes = post.getLikes();
        if(likes.contains(user)){
            likes = likes.stream().filter(u -> u.getUserId() != user.getUserId()).collect(Collectors.toSet());
        } else {
            likes.add(user);
        }
        post.setLikes(likes);

        return postRepo.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }
    
    public List<Post> getAllFeedPosts(Integer userId) {
        return postRepo.findFeedPostsNotByUserId(userId, LocalDateTime.now());
    }

    public Post getPostById(Integer id) {
        // TODO: setup custom exception for posts that dont exist
        return postRepo.findById(id).orElseThrow(PostDoesNotExistException::new);
    }

    public Set<Post> getAllPostsByAuthor(ApplicationUser author) {
        Set<Post> usersPosts = postRepo.findByAuthor(author).orElse(new HashSet<>());

        return usersPosts;
    }

    // public Page<Post> getAllPostsByAuthors(Set<ApplicationUser> authors,
    // LocalDateTime sessionStart, Integer page){
    // // Get the next 100 posts starting on page specified in the request
    // Pageable pageable = PageRequest.of(page, 100,
    // Sort.by("postedDate").descending());
    // return postRepo.findPostsByAuthors(authors, sessionStart, pageable);
    // }

    public Page<Post> getFeedPage(Integer userId, LocalDateTime sessionStart, Integer page) {
        Pageable pageable = PageRequest.of(page, 100);
        System.out.println(sessionStart);
        return postRepo.findFeedPosts(userId, sessionStart, pageable);
    }

    public void deletePost(Post p) {
        postRepo.delete(p);
    }
}
