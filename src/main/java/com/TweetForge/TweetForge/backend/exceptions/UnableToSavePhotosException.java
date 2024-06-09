package com.TweetForge.TweetForge.backend.exceptions;

public class UnableToSavePhotosException extends Exception{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnableToSavePhotosException() {
        super("Unable to save the supplied photo");
    }
}
