package com.TweetForge.TweetForge.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;


@Entity
@Table(name="users")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @Column(name="dob")
    private Date dateOfBirth;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String bio;

    private String nickname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="profile_picture", referencedColumnName="image_id")
    private Image profilePicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="banner_picture", referencedColumnName="image_id")
    private Image bannerPicture;

    @Column(name="verified_account", nullable = true)
    private boolean verifiedAccount;

    @ManyToOne
    @JoinColumn(name="organization_id", nullable = true)
    private Image organization;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="following",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns =  {@JoinColumn(name="following_id")}
    )

    @JsonIgnore
    private Set<ApplicationUser> following;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="followers",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns =  {@JoinColumn(name="followers_id")}
    )

    @JsonIgnore
    private Set<ApplicationUser> followers;


    /* Security related */

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="user_role_junction",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
//     this code above defines a many-to-many relationship between two entities using a join table, with foreign key columns that reference the primary keys of the two entities. The fetch strategy is set to FetchType.EAGER, which means that the related entities will be fetched from the database as soon as the owning entity is queried.

    private Set<Role> authorities;

    private Boolean enabled;

    @Column(nullable = true)
    @JsonIgnore
    private Long verificationCode;

//    public ApplicationUser(Set<Role> authorities) {
//        this.authorities = authorities;
//    }

    public ApplicationUser() {
        this.authorities = new HashSet<>();
        this.following = new HashSet<>();
        this.followers = new HashSet<>();
        this.enabled = true;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Long verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Image getBannerPicture() {
        return bannerPicture;
    }

    public void setBannerPicture(Image bannerPicture) {
        this.bannerPicture = bannerPicture;
    }

    public Set<ApplicationUser> getFollowing() {
        return following;
    }

    public void setFollowing(Set<ApplicationUser> following) {
        this.following = following;
    }

    public Set<ApplicationUser> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<ApplicationUser> followers) {
        this.followers = followers;
    }

    public boolean isVerifiedAccount() {
        return verifiedAccount;
    }

    public void setVerifiedAccount(boolean verifiedAccount) {
        this.verifiedAccount = verifiedAccount;
    }

    public Image getOrganization() {
        return organization;
    }

    public void setOrganization(Image organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profilePicture=" + profilePicture +
                ", bannerPicture=" + bannerPicture +
                ", verifiedAccount=" + verifiedAccount +
                ", organization=" + organization +
                ", following=" + following +
                ", followers=" + followers +
                ", authorities=" + authorities +
                ", enabled=" + enabled +
                ", verificationCode=" + verificationCode +
                '}';
    }
}
