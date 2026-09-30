package com.lorem_ipsum.thesis.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("betreuer_profile")
public record BetreuerDto(@Id Integer id, @Column("github_id") String githubID, String name, String email,
                          List<String> tags) {
    public BetreuerDto {
        tags = (tags == null) ? List.of() : List.copyOf(tags);
    }
}



