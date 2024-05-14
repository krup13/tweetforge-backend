package com.TweetForge.TweetForge.backend.controllers;

import com.TweetForge.TweetForge.backend.exceptions.EmailAlreadyTakenException;
import com.TweetForge.TweetForge.backend.exceptions.EmailFailedToSendException;
import com.TweetForge.TweetForge.backend.exceptions.UserDoesNotExistException;
import com.TweetForge.TweetForge.backend.models.ApplicationUser;
import com.TweetForge.TweetForge.backend.models.RegistrationObject;
import com.TweetForge.TweetForge.backend.services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<String> handleEmailTaken() {
        return new ResponseEntity<String>("Email provided already in use", HttpStatus.CONFLICT);
    }

    //go to http://localhost:8000/auth/register
    @PostMapping("/register")
    public ApplicationUser regiserUser(@RequestBody RegistrationObject registrationObject) {
        return userService.registerUser(registrationObject);
    }

    @ExceptionHandler({UserDoesNotExistException.class})
    public ResponseEntity<String> handleUserDoesNotExist() {
        return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
    }


    @PutMapping("/update/phone")
    public ApplicationUser updatePhoneNumber(@RequestBody LinkedHashMap<String, String> body) {

        String username = body.get("username");
        String phone = body.get("phone");

        ApplicationUser user = userService.getUserByUsername(username);

        user.setPhoneNumber(phone);

        return userService.updateUser(user);

    }

    @ExceptionHandler({EmailFailedToSendException.class})
    public ResponseEntity<String> handleFailedEmail(){
        return new ResponseEntity<String>("Email failed to send, try again in a moment", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerificationCode(@RequestBody LinkedHashMap<String, String> body) {
        userService.generateEmailVerification(body.get("username"));

        return new ResponseEntity<String>("Verification code generated, email sent", HttpStatus.OK);
    }


}
