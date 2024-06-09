package com.TweetForge.TweetForge.backend.exceptions;

public class FollowException extends Throwable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public FollowException(){
        super("Users cannot follow themselves");
    }
}
