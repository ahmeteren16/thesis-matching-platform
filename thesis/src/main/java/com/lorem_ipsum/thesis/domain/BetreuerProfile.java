package com.lorem_ipsum.thesis.domain;


import org.springframework.data.annotation.Version;

import java.util.List;


public class BetreuerProfile {


    private Integer id;
    private String githubID;
    private String name;
    private String email;
    private List<String> tags;
    @Version
    private Long version;

    public BetreuerProfile(Integer id, String githubId, String name, String email, List<String> tags) {
        this.id = id;
        this.githubID = githubId;
        this.name = name;
        this.email = email;
        this.tags = (tags == null) ? List.of() : List.copyOf(tags);
    }

    public static BetreuerProfile createBetreuer(String githubId, String name, String email, List<String> tags) {
        return new BetreuerProfile(null, githubId, name, email, tags);
    }


    public Integer getId() {
        return id;
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

    public List<String> getTags() {
        return tags;
    }


}
