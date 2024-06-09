package com.TweetForge.TweetForge.backend.controllers;

import com.TweetForge.TweetForge.backend.exceptions.UnableToResolvePhotoException;
import com.TweetForge.TweetForge.backend.exceptions.UnableToSavePhotosException;
import com.TweetForge.TweetForge.backend.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@CrossOrigin("*")

public class ImageController {

    public final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @ExceptionHandler({UnableToSavePhotosException.class, UnableToResolvePhotoException.class})
    public ResponseEntity<String> handlePhotosException(Exception e) {
        return new ResponseEntity<String>("Unable to process the photo", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/{fileName")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String fileName) throws UnableToResolvePhotoException {
        byte[] imageBytes = imageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(imageService.getImageType(fileName)))
                .body(imageBytes);
    }
}
