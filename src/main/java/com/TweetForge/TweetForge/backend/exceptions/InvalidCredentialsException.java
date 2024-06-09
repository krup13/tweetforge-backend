package com.TweetForge.TweetForge.backend.exceptions;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException(){
        super("Username or password does not exist");
    }
}
