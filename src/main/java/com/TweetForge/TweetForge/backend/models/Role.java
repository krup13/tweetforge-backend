package com.TweetForge.TweetForge.backend.models;
import jakarta.persistence.*;

@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private Integer role_Id;

    private String authority;

    public Role() {
    }

    public Role(Integer role_Id, String authority) {
        this.role_Id = role_Id;
        this.authority = authority; //sign-in authentication
    }

    public Integer getRole_Id() {
        return role_Id;
    }

    public void setRole_Id(Integer role_Id) {
        this.role_Id = role_Id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Role{" +
                "role_Id=" + role_Id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
