package com.TweetForge.TweetForge.backend.exceptions;

public class EmailAlreadyTakenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmailAlreadyTakenException() {
        super("Email is already taken");
    }
}
