package com.TweetForge.TweetForge.backend.exceptions;

import java.io.Serial;

public class EmailFailedToSendException extends RuntimeException{
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public EmailFailedToSendException() {
        super("The email failed to send.");
    }
}
