package com.TweetForge.TweetForge.backend.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.TweetForge.TweetForge.backend.exceptions.UnableToResolvePhotoException;
import com.TweetForge.TweetForge.backend.exceptions.UnableToSavePhotoException;
import com.TweetForge.TweetForge.backend.models.Image;
import com.TweetForge.TweetForge.backend.repositories.ImageRepository;

@Service
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    private static final File DIRECTORY = new File("C:\\Users\\Ethan\\Desktop\\LetsBuildTwitter\\fwitter-backend\\img");
    private static final String URL = "http://localhost:8000/images/";

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<Image> getImageByImageName(String name) {
        return imageRepository.findByImageName(name);
    }
    public Image saveGifFromPost(Image image) {
        return imageRepository.save(image);
    }

    public Image uploadImage(MultipartFile file, String prefix) throws UnableToSavePhotoException{
        try {
            //The content type from the request looks something like this img/jpeg
            String extension = "." + file.getContentType().split("/")[1];

            File img = File.createTempFile(prefix, extension, DIRECTORY);

            file.transferTo(img);

            String imageURL = URL + img.getName();

            Image i = new Image(img.getName(), file.getContentType(), img.getPath(), imageURL);

            Image saved = imageRepository.save(i);

            return saved;

        } catch (IOException e) {
            e.printStackTrace();
            throw new UnableToSavePhotoException();
        }
    }

    public Image createOrganization(MultipartFile file, String organizationName) throws UnableToSavePhotoException {
        try{
            String extension = "." + file.getContentType().split("/")[1];
            File orgImg = new File(DIRECTORY + "\\" + organizationName + extension);
            orgImg.createNewFile();
            file.transferTo(orgImg);

            String imageURL = URL + orgImg.getName();

            Image i = new Image(orgImg.getName(), file.getContentType(), orgImg.getPath(), imageURL);

            return imageRepository.save(i);

        } catch(IOException e){
            e.printStackTrace();
            throw new UnableToSavePhotoException();
        }
    }

    public byte[] downloadImage(String filename) throws UnableToResolvePhotoException{
        try {
            Image image = imageRepository.findByImageName(filename).get();

            String filePath = image.getImagePath();

            byte[] imageBytes = Files.readAllBytes(new File(filePath).toPath());

            return imageBytes;
        } catch(IOException e) {
            throw new UnableToResolvePhotoException();
        }
    }

    public String getImageType(String fileName) {
        Image image = imageRepository.findByImageName(fileName).get();

        return image.getImageType();
    }

}
