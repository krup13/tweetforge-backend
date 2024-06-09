package com.TweetForge.TweetForge.backend.services;


import com.TweetForge.TweetForge.backend.dto.CreatePostDTO;
import com.TweetForge.TweetForge.backend.exceptions.PostDoesNotExistException;
import com.TweetForge.TweetForge.backend.exceptions.UnableToCreatePostException;
import com.TweetForge.TweetForge.backend.models.*;
import com.TweetForge.TweetForge.backend.repositories.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.Multipart;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepo;
    private final ImageService imageService;
    private final PollService pollService;

    @Autowired
    public PostService(PostRepository postRepo, ImageService imageService, PollService pollService){
        this.postRepo = postRepo;
        this.imageService = imageService;
        this.pollService = pollService;
    }

    public Post createPost(CreatePostDTO dto){

        Image savedGif;

        //If true, there is a single gif from tenor
        if(dto.getImages() !=null && dto.getImages().size() > 0){
            List<Image> gifList = dto.getImages();
            Image gif = gifList.get(0);
            gif.setImagePath(gif.getImageURL());

            savedGif = imageService.saveGifFromPost(gif);
            gifList.remove(0);
            gifList.add(savedGif);
            dto.setImages(gifList);
        }

        //If true, there is a Poll that needs to be created
        Poll savedPoll = null;
        //System.out.println(dto);
        if(dto.getPoll() != null){
            Poll p = new Poll();
            p.setEndTime(dto.getPoll().getEndTime());
            p.setChoices(new ArrayList<>());
            savedPoll = pollService.generatePoll(p);
            List<PollChoice> pollChoices = new ArrayList<PollChoice>();
            List<PollChoice> choices = dto.getPoll().getChoices();
            for(int i=0; i<choices.size(); i++){
                PollChoice choice = choices.get(i);
                choice.setPoll(savedPoll);
                choice = pollService.generateChoice(choice);
                System.out.println(choice);
            }

            savedPoll.setChoices(pollChoices);
            savedPoll = pollService.generatePoll(savedPoll);

            System.out.println(savedPoll);


        }

        Post p = new Post();
        p.setContent(dto.getContent());
        if(dto.getScheduled()){
            p.setPostedDate(dto.getScheduledDate());
        }
        else{
            p.setPostedDate(new Date());
        }
        p.setAuthor(dto.getAuthor());
        p.setReplies(dto.getReplies());
        p.setSchedule(dto.getScheduled());
        p.setScheduleDate(dto.getScheduledDate());
        p.setAudience(dto.getAudience());
        p.setReplyRestriction(dto.getReplyRestriction());
        p.setImages(dto.getImages());
        p.setPoll(savedPoll);

        try{
            Post posted = postRepo.save(p);
            return posted;
        }catch(Exception e){
            //TODO: Setup custom exceptional
            throw new UnableToCreatePostException();
        }

    }

    public Post createdMediaPost(String post, List<MultipartFile> files){
        CreatePostDTO dto = new CreatePostDTO();

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            dto = objectMapper.readValue(post, CreatePostDTO.class);

            Post p = new Post();
            p.setContent(dto.getContent());
            if(dto.getScheduled()){
                p.setPostedDate(dto.getScheduledDate());
            }
            else{
                p.setPostedDate(new Date());
            }
            p.setAuthor(dto.getAuthor());
            p.setReplies(dto.getReplies());
            p.setSchedule(dto.getScheduled());
            p.setScheduleDate(dto.getScheduledDate());
            p.setAudience(dto.getAudience());
            p.setReplyRestriction(dto.getReplyRestriction());

            //Upload the images that got passed
            List<Image> postImages = new ArrayList<>();

            for(int i=0; i<files.size(); i++){
                Image postImage = imageService.uploadImage(files.get(i), "post");
                postImages.add(postImage);
            }

            p.setImages(postImages);
            return postRepo.save(p);

        }catch(Exception e){
            throw new UnableToCreatePostException();
        }
    }

    public List<Post> getAllPosts(){
        return postRepo.findAll();
    }

    public Post getPostById(Integer id){
        //TODO: setup custom exception for post that dont exist
        return postRepo.findById(id).orElseThrow(PostDoesNotExistException::new);
    }

    public Set<Post> getAllPostsByAuthor(ApplicationUser author) {
        Set<Post> usersPosts = postRepo.findByAuthor(author).orElse(new HashSet<>());

        return usersPosts;
    }

    public List<Post> getAllPostsByAuthors(Set<ApplicationUser> authors) {
        return postRepo.findPostsByAuthors(authors);
    }

    public void deletePost(Post p){
        postRepo.delete(p);
    }

}
