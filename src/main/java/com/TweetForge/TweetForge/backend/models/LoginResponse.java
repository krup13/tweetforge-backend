package com.TweetForge.TweetForge.backend.models;

public class LoginResponse {
    private ApplicationUser user;
    private String token;

    public LoginResponse(ApplicationUser user, String token) {
        super();
        this.user = user;
        this.token = token;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString(){
        return "LoginResponse [user=" + user + ", token=" + token + "]";
    }
}
