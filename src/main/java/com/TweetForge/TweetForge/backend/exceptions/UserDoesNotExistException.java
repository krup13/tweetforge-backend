package com.TweetForge.TweetForge.backend.exceptions;

public class UserDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserDoesNotExistException() {
        super("user does not exist");
    }
}
