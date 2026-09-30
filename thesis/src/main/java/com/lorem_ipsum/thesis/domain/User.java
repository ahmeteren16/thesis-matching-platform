package com.lorem_ipsum.thesis.domain;

public class User {


    private Integer id;
    private String githubID;
    private String name;
    private String email;

    public User(String githubID, String name, String email) {
        this.githubID = githubID;
        this.name = name;
        this.email = email;
    }

    public User() {

    }


    public String getGithubID() {
        return githubID;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public Integer getId() {
        return id;
    }
}
