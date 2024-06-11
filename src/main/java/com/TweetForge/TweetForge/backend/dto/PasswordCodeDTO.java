package com.TweetForge.TweetForge.backend.dto;

public class PasswordCodeDTO {

    private int code;
    private String email;

    public PasswordCodeDTO() {
        super();
    }

    public PasswordCodeDTO(int code, String email) {
        super();
        this.code = code;
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "PasswordCodeDTO [code=" + code + ", email=" + email + "]";
    }
}
