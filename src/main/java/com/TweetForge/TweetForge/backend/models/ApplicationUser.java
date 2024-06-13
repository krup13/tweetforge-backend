package com.TweetForge.TweetForge.backend.models;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="users")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(unique=true)
    private String email;

    private String phone;

    @Column(name="dob")
    private Date dateOfBirth;

    @Column(unique=true)
    private String username;

    @JsonIgnore
    private String password;

    private String bio;

    private String nickname;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="profile_picture", referencedColumnName="image_id")
    private Image profilePicture;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="banner_picture", referencedColumnName="image_id")
    private Image bannerPicture;

    @Column(name="verified_account", nullable = true)
    private Boolean verifiedAccount;

    @Column(name="private_account", nullable=true)
    private Boolean privateAccount;

    @ManyToOne
    @JoinColumn(name="organization_id", nullable = true)
    private Image organization;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="following",
            joinColumns= {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="following_id")}
    )
    @JsonIgnore
    private Set<ApplicationUser> following;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="followers",
            joinColumns= {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="follower_id")}
    )
    @JsonIgnore
    private Set<ApplicationUser> followers;

    /* Security related */

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="user_role_junction",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    private Set<Role> authorities;

    private Boolean enabled;

    @Column(nullable=true)
    @JsonIgnore
    private Long verification;

    public ApplicationUser() {
        this.authorities = new HashSet<>();
        this.following = new HashSet<>();
        this.followers = new HashSet<>();
        this.enabled = false;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Long getVerification() {
        return verification;
    }

    public void setVerification(Long verification) {
        this.verification = verification;
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

    public Boolean isVerifiedAccount() {
        if(verifiedAccount == null){
            return false;
        }
        return verifiedAccount;
    }

    public void setVerifiedAccount(Boolean verifiedAccount) {
        this.verifiedAccount = verifiedAccount;
    }

    public Image getOrganization() {
        return organization;
    }

    public void setOrganization(Image organization) {
        this.organization = organization;
    }

    public Boolean isPrivateAccount() {
        if(privateAccount == null){
            return false;
        }
        return privateAccount;
    }

    public void setPrivateAccount(Boolean privateAccount) {
        this.privateAccount = privateAccount;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profilePicture=" + profilePicture +
                ", bannerPicture=" + bannerPicture +
                ", verifiedAccount=" + verifiedAccount +
                ", privateAccount=" + privateAccount +
                ", organization=" + organization +
                ", following=" + following +
                ", followers=" + followers +
                ", authorities=" + authorities +
                ", enabled=" + enabled +
                ", verification=" + verification +
                '}';
    }
}
