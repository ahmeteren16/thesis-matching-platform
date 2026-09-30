package com.lorem_ipsum.thesis.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BetreuerAddRequest(
        @NotBlank @Size(min = 2, max = 50) String name,
        @Size(max = 50) String githubID,
        @NotBlank @Email String email,
        List<@Size(max = 30) String> tags
) {}
